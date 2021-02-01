package lijie.test.flinkComTest.streaming;


import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.*;
import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import lijie.test.flinkComTest.sink.*;
import lijie.test.flinkComTest.streaming.process.*;
import lijie.test.flinkComTest.util.ProcessUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.OutputTag;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class RobotCallRealTimeStatistics {

    private static Logger logger = LoggerFactory.getLogger(RobotCallRealTimeStatistics.class.getClass());

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        if (!ProcessUtil.jobArgsCheck(args, properties, logger)) {
            return;
        }
        String profile = properties.getProperty("profile");
        String jobName = RobotCallRealTimeStatistics.class.getSimpleName() + "-" + profile;
        // kafka数据源参数
        String bootstrapServers = properties.getProperty("kafka.bootstrap.servers");
        String groupId = properties.getProperty("kafka.group.callRealTimeStatistics.id");
        String topic = properties.getProperty("kafka.callRealTimeStatistics.topic");
        String securityProtocol = properties.getProperty("kafka.security.protocol");
        String truststoreLocation = properties.getProperty("kafka.ssl.truststore.location");
        String truststorePassword = properties.getProperty("kafka.ssl.truststore.password");
        // 数据库配置
        String dbDriver;
        String dbUrl;
        String dbUser;
        String dbPass;
        String flowDbDriver;
        String flowDbUrl;
        String flowDbUser;
        String flowDbPass;
        String dbType = properties.getProperty("database.callStatistics.dbType");
        String flowDbType = properties.getProperty("database.flowweb.dbType");
        if (Constants.DBType.MYSQL.getValue().equals(dbType)) {
            dbDriver = properties.getProperty("database.callStatistics.mysql.dbDriver");
            dbUrl = properties.getProperty("database.callStatistics.mysql.dbUrl");
            dbUser = properties.getProperty("database.callStatistics.mysql.dbUser");
            dbPass = properties.getProperty("database.callStatistics.mysql.dbPass");
        } else if (Constants.DBType.ORACLE.getValue().equals(dbType)) {
            dbDriver = properties.getProperty("database.callStatistics.oracle.dbDriver");
            dbUrl = properties.getProperty("database.callStatistics.oracle.dbUrl");
            dbUser = properties.getProperty("database.callStatistics.oracle.dbUser");
            dbPass = properties.getProperty("database.callStatistics.oracle.dbPass");
        } else {
            logger.error("请配置业务数据库类型!\n");
            return;
        }
        if (Constants.DBType.MYSQL.getValue().equals(flowDbType)) {
            flowDbDriver = properties.getProperty("database.flowweb.mysql.dbDriver");
            flowDbUrl = properties.getProperty("database.flowweb.mysql.dbUrl");
            flowDbUser = properties.getProperty("database.flowweb.mysql.dbUser");
            flowDbPass = properties.getProperty("database.flowweb.mysql.dbPass");
        } else if (Constants.DBType.ORACLE.getValue().equals(flowDbType)) {
            flowDbDriver = properties.getProperty("database.flowweb.oracle.dbDriver");
            flowDbUrl = properties.getProperty("database.flowweb.oracle.dbUrl");
            flowDbUser = properties.getProperty("database.flowweb.oracle.dbUser");
            flowDbPass = properties.getProperty("database.flowweb.oracle.dbPass");
        } else {
            logger.error("请配置流程数据库类型!\n");
            return;
        }
        // 文件检查点路径
        String fileCheckpointPath = properties.getProperty("flink.fileCheckpoint.path");
        // 流试计算执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 若设置了检查点本地路径，则开启检查点，且状态后端为本地文件
        if (!StringUtils.isNullOrWhitespaceOnly(fileCheckpointPath)) {
            Path backendPath = new Path(fileCheckpointPath + System.getProperty("file.separator") + jobName);
            StateBackend fsStateBackend = new FsStateBackend(backendPath);
            env.setStateBackend(fsStateBackend);
            env.enableCheckpointing(30000);
            env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        }
        // kafka数据源
        Properties kafkaPro = new Properties();
        kafkaPro.setProperty("bootstrap.servers", bootstrapServers);
        kafkaPro.setProperty("group.id", groupId);


        if (org.apache.commons.lang3.StringUtils.isNotEmpty(securityProtocol)) {
            kafkaPro.setProperty("security.protocol", securityProtocol);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(truststoreLocation)) {
            kafkaPro.setProperty("ssl.truststore.location", truststoreLocation);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(truststorePassword)) {
            kafkaPro.setProperty("ssl.truststore.password", truststorePassword);
        }
        DataStream<String> rowData = env.addSource(new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), kafkaPro));
        rowData.print("rowData");

        SingleOutputStreamOperator<ConversationInfo> msgData = rowData.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                return !StringUtils.isNullOrWhitespaceOnly(s);
            }
        }).map(new KafkaMsgConverter(dbDriver, dbUrl, dbUser, dbPass, flowDbDriver, flowDbUrl, flowDbUser, flowDbPass));
        msgData.print("msgData");

        // 访问基本统计
        SingleOutputStreamOperator<VisitDataStatisticsOutput> visitDataStatistics = msgData.filter((FilterFunction<ConversationInfo>) value -> {
            if (StringUtils.isNullOrWhitespaceOnly(value.getDayStr()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSessionId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysCompanyId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysUserId())) {
                return false;
            }
            return true;
        }).keyBy("dayStr", "sysCompanyId", "sysUserId").process(new VisitDataCountWindowProcess());

        visitDataStatistics.print("visitDataStatistics");
        visitDataStatistics.addSink(new VisitDataStatisticsJdbcSink(dbDriver, dbUrl, dbUser, dbPass));

        // 渠道分析
        SingleOutputStreamOperator<VisitChannelStatisticsOutput> visitChannelStatistics = msgData.filter((FilterFunction<ConversationInfo>) value -> {
            if (StringUtils.isNullOrWhitespaceOnly(value.getDayStr()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getChannel()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSessionId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysCompanyId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysUserId())) {
                return false;
            }
            return true;
        }).keyBy("dayStr", "sysCompanyId", "sysUserId", "channel").process(new VisitChannelCountWindowProcess());

        visitChannelStatistics.print("visitChannelStatistics");
        visitChannelStatistics.addSink(new VisitChannelStatisticsJdbcSink(dbDriver, dbUrl, dbUser, dbPass));

        // 意图分析
        SingleOutputStreamOperator<VisitIntentionStatisticsOutput> visitIntentionStatistics = msgData.filter((FilterFunction<ConversationInfo>) value -> {
            if (StringUtils.isNullOrWhitespaceOnly(value.getDayStr()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getUserLabel()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSessionId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysCompanyId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysUserId())) {
                return false;
            }
            return true;
        }).keyBy("dayStr", "sysCompanyId", "sysUserId", "userLabel").process(new VisitIntentionCountWindowProcess());

        visitIntentionStatistics.print("visitIntentionStatistics");
        visitIntentionStatistics.addSink(new VisitIntentionStatisticsJdbcSink(dbDriver, dbUrl, dbUser, dbPass));

        // 热点问题分析
        SingleOutputStreamOperator<HotIssueStatisticsOutput> hotIssueStatistics = msgData.filter((FilterFunction<ConversationInfo>) value -> {
            if (StringUtils.isNullOrWhitespaceOnly(value.getDayStr()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getRobotId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getStandardInput()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getNodeLabel()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysCompanyId()) ||
                    StringUtils.isNullOrWhitespaceOnly(value.getSysUserId())) {
                return false;
            }
            if (Constants.ReplyType.NOANSWER.getValue().equals(value.getNodeLabel()) ||
                    Constants.ReplyType.CLARIFY.getValue().equals(value.getNodeLabel())) {
                return false;
            }
            return true;
        }).keyBy("dayStr", "sysCompanyId", "sysUserId", "robotId", "standardInput", "nodeLabel").process(new HotIssueCountWindowProcess());

        hotIssueStatistics.print("hotIssueStatistics");
        hotIssueStatistics.addSink(new HotIssueStatisticsJdbcSink(dbDriver, dbUrl, dbUser, dbPass));

        // 服务率统计
        SingleOutputStreamOperator<ServiceRateStatisticsOutput> serviceRateStatistics = msgData
                .filter((FilterFunction<ConversationInfo>) value -> {
                    if (StringUtils.isNullOrWhitespaceOnly(value.getDayStr()) ||
                            StringUtils.isNullOrWhitespaceOnly(value.getRobotId()) ||
                            StringUtils.isNullOrWhitespaceOnly(value.getNodeLabel()) ||
                            StringUtils.isNullOrWhitespaceOnly(value.getSysCompanyId()) ||
                            StringUtils.isNullOrWhitespaceOnly(value.getSysUserId())) {
                        return false;
                    }
                    return true;
                }).map((MapFunction<ConversationInfo, ConversationInfo>) value -> {
                    if (!Constants.ReplyType.NOANSWER.getValue().equals(value.getNodeLabel()) &&
                            !Constants.ReplyType.CLARIFY.getValue().equals(value.getNodeLabel())) {
                        value.setNodeLabel(Constants.ReplyType.ANSWER.getValue());
                    }
                    return value;
                }).keyBy("dayStr", "sysCompanyId", "sysUserId", "robotId", "nodeLabel").process(new ServiceRateWindowProcess());

        DataStream<ConversationInfo> noAnswerCon = serviceRateStatistics.getSideOutput(new OutputTag<ConversationInfo>("noAnswerConversation") {
        });
        serviceRateStatistics.print("serviceRateStatistics");
        noAnswerCon.print("noAnswerCon");
        serviceRateStatistics.addSink(new ServiceRateStatisticsJdbcSink(dbDriver, dbUrl, dbUser, dbPass));
        noAnswerCon.addSink(new ServiceRateReportJdbcSink(dbDriver, dbUrl, dbUser, dbPass));

        env.execute(jobName);
    }
}
