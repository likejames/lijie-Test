package lijie.test.flinkComTest.batch;


import lijie.test.flinkComTest.batch.process.BatchCorpusCntInfoRowMapper;
import lijie.test.flinkComTest.batch.process.BatchCorpusInfoRowMapper;
import lijie.test.flinkComTest.batch.process.BatchLearnTaskInfoRowMapper;
import lijie.test.flinkComTest.batch.sql.SplitLen;
import lijie.test.flinkComTest.batch.sql.StringToZeroOrOne;
import lijie.test.flinkComTest.batch.sql.TimeToDayString;
import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.BatchTableCorpusInfo;
import lijie.test.flinkComTest.entity.BatchTableCorpusTotalCntInfo;
import lijie.test.flinkComTest.entity.BatchTableLearnTaskInfo;
import lijie.test.flinkComTest.util.CommonUtil;
import lijie.test.flinkComTest.util.ProcessUtil;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCAppendTableSink;
import org.apache.flink.api.java.io.jdbc.JDBCAppendTableSinkBuilder;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class RobotCallBatchStatistics {

    private static Logger logger = LoggerFactory.getLogger(RobotCallBatchStatistics.class.getClass());

    public static String learnTaskLoadSql = "SELECT DISTINCT u.username, t3.flow_id, t3.flow_name, t3.robot_id, t1.operator, u.flow_user_id as account_id, t1.mark_time, u.id as user_id " +
            "FROM learn_task_sample_session t1 \n" +
            "INNER JOIN learn_task_sample t2 ON t1.learn_task_sample_id=t2.id \n" +
            "INNER JOIN learn_task t3 ON t2.learn_task_id=t3.id \n" +
            "INNER JOIN sys_user u ON t2.user_id=u.id \n" +
            "WHERE %s " +
            "AND t1.operator IS NOT NULL AND t1.mark_time IS NOT NULL";

    public static String corpusInfoLoadSql = "SELECT * FROM \n" +
            "(SELECT c.inputs AS content, r.flow_id, c.account_id, c.create_timestamp, r.id as robot_id, r.title FROM interactive_corpus c \n" +
            "INNER JOIN corpus_robot r ON r.knowledge_base_id=c.knowledge_base_id AND r.account_id=c.account_id \n" +
            "UNION ALL \n" +
            "SELECT cc.keywords AS content, cc.flow_id, cc.account_id, cc.create_timestamp, cr.id as robot_id, cr.title FROM condition_corpus cc " +
            "INNER JOIN corpus_robot cr ON cr.flow_id=cc.flow_id AND cr.account_id=cc.account_id \n" +
            ") main \n" +
            "WHERE %s " +
            "AND main.create_timestamp is not null";

    public static final String corpusTotalCntInfoLoadSql = "SELECT main.account_id, main.flow_id, count(*) cnt FROM \n" +
            "(SELECT c.inputs AS content, r.flow_id, c.account_id, c.create_timestamp FROM interactive_corpus c \n" +
            "INNER JOIN corpus_robot r ON r.knowledge_base_id=c.knowledge_base_id AND r.account_id=c.account_id \n" +
            "UNION ALL \n" +
            "SELECT keywords AS content, flow_id, account_id, create_timestamp FROM condition_corpus) main \n" +
            "WHERE main.create_timestamp is not null \n" +
            "GROUP BY main.account_id, main.flow_id";

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        if(!ProcessUtil.jobArgsCheck(args, properties, logger)){
            return;
        }
        String profile = properties.getProperty("profile");
        String jobName = RobotCallBatchStatistics.class.getSimpleName() + "-" + profile;
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
        if(Constants.DBType.MYSQL.getValue().equals(dbType)){
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
        if(Constants.DBType.MYSQL.getValue().equals(flowDbType)){
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

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String all = parameterTool.get("all");
        String finalLearnTaskLoadSql = null;
        String finalCorpusInfoLoadSql = null;
        if(StringUtils.isNullOrWhitespaceOnly(all)){
            System.out.println("统计当天的数据");
            // 当前日期
            Date cDate = CommonUtil.getDateByTimeStamp(System.currentTimeMillis()/1000);
            String cDay = new SimpleDateFormat(Constants.dayDateFormatStr).format(cDate);
            String aDay = CommonUtil.getDayAfter(cDay);
            if(Constants.DBType.MYSQL.getValue().equals(dbType)){
                finalLearnTaskLoadSql = String.format(learnTaskLoadSql, "t1.mark_time>='" + cDay + "' AND t1.mark_time<='" + aDay + "'");
            } else if (Constants.DBType.ORACLE.getValue().equals(dbType)) {
                finalLearnTaskLoadSql = String.format(learnTaskLoadSql, "t1.mark_time>=TO_DATE('" + cDay + "','yyyy-mm-dd hh24:mi:ss') AND t1.mark_time<=TO_DATE('" + cDay + "','yyyy-mm-dd hh24:mi:ss')");
            }
            if(Constants.DBType.MYSQL.getValue().equals(flowDbType)){
                finalCorpusInfoLoadSql = String.format(corpusInfoLoadSql, "main.create_timestamp>='" + cDay + "' AND main.create_timestamp<='" + aDay + "'");
            } else if (Constants.DBType.ORACLE.getValue().equals(flowDbType)) {
                finalCorpusInfoLoadSql = String.format(corpusInfoLoadSql, "main.create_timestamp>=TO_DATE('" + cDay + "','yyyy-mm-dd hh24:mi:ss') AND main.create_timestamp<=TO_DATE('" + cDay + "','yyyy-mm-dd hh24:mi:ss')");
            }
        } else {
            System.out.println("统计所有数据");
            finalLearnTaskLoadSql = String.format(learnTaskLoadSql, "1=1");
            finalCorpusInfoLoadSql = String.format(corpusInfoLoadSql, "1=1");
        }
        System.out.println("finalLearnTaskLoadSql");
        System.out.println(finalLearnTaskLoadSql);
        System.out.println("finalCorpusInfoLoadSql");
        System.out.println(finalCorpusInfoLoadSql);

        // 获取执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
////        BatchTableEnvironment tableEnv = BatchTableEnvironment.create(env);
        BatchTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(env);

        // 读取数据
        // 话术学习信息
        RowTypeInfo learnTaskInfo = BatchTableLearnTaskInfo.buildRowTypeInfo();
        DataSet<Row> learnTaskDbData =
                env.createInput(
                        ProcessUtil.buildJDBCInputFormat(dbDriver, dbUrl, dbUser, dbPass, finalLearnTaskLoadSql, learnTaskInfo)
                );
        // 语料信息数据
        RowTypeInfo corpusInfo = BatchTableCorpusInfo.buildRowTypeInfo();
        DataSet<Row> corpusDbData =
                env.createInput(
                        ProcessUtil.buildJDBCInputFormat(flowDbDriver, flowDbUrl, flowDbUser, flowDbPass, finalCorpusInfoLoadSql, corpusInfo)
                );
        // 语料信息总数数据
        RowTypeInfo corpusCntInfo = BatchTableCorpusTotalCntInfo.buildRowTypeInfo();
        DataSet<Row> corpusCntDbData =
                env.createInput(
                        ProcessUtil.buildJDBCInputFormat(flowDbDriver, flowDbUrl, flowDbUser, flowDbPass, corpusTotalCntInfoLoadSql, corpusCntInfo)
                );
        // 映射样例类
        MapOperator<Row, BatchTableLearnTaskInfo> learnTaskData = learnTaskDbData.map(new BatchLearnTaskInfoRowMapper());
        MapOperator<Row, BatchTableCorpusInfo> corpusData = corpusDbData.map(new BatchCorpusInfoRowMapper());
        MapOperator<Row, BatchTableCorpusTotalCntInfo> corpusCntData = corpusCntDbData.map(new BatchCorpusCntInfoRowMapper());
        // 注册UDF
        tableEnv.registerFunction("timeToDayString", new TimeToDayString());
        tableEnv.registerFunction("splitLen", new SplitLen("\n"));
        tableEnv.registerFunction("stringToZeroOrOne", new StringToZeroOrOne());
        // 注册话术学习临时表
        tableEnv.registerDataSet("learnTask", learnTaskData);
        // 注册语料相关临时表
        tableEnv.registerDataSet("corpus", corpusData);
        // 知识总量
        tableEnv.registerDataSet("corpusCnt", corpusCntData);
        // 新增用户语料
        Table corpusCntDay = tableEnv.sqlQuery("select timeToDayString(createTimestamp) as dayStr, accountId, flowId, CAST(count(content) AS INTEGER) as ins, CAST(0 AS INTEGER) as splitCnt from corpus group by timeToDayString(createTimestamp), accountId, flowId");
        tableEnv.registerTable("corpusCntDay", corpusCntDay);
        // 新增意图
        Table splitCorpusCntDay = tableEnv.sqlQuery("select timeToDayString(createTimestamp) as dayStr, accountId, flowId, CAST(0 AS INTEGER) as ins, CAST(sum(splitLen(content)) AS INTEGER) as splitCnt from corpus group by timeToDayString(createTimestamp), accountId, flowId");
        tableEnv.registerTable("splitCorpusCntDay", splitCorpusCntDay);
        // 标注数量
        Table markCnt = tableEnv.sqlQuery("select timeToDayString(markTime) as dayStr, accountId, flowId, CAST(0 AS INTEGER) as ins, CAST(0 AS INTEGER) as splitCnt, CAST(0 AS INTEGER) as cnt, sum(stringToZeroOrOne(operator)) as markCnt from learnTask where markTime is not null group by timeToDayString(markTime), accountId, flowId");
        tableEnv.registerTable("markCnt", markCnt);
        // 综合语料信息
        Table corpusCountInfo = tableEnv.sqlQuery("select main.*, COALESCE(t2.cnt, 0) as cnt, CAST(0 AS INTEGER) as markCnt from" +
                "(select dayStr, accountId, flowId, sum(ins) as ins, sum(splitCnt) as splitCnt " +
                "   from (select * from corpusCntDay UNION select * from splitCorpusCntDay) t1 group by dayStr, accountId, flowId) main " +
                "left join corpusCnt t2 on main.accountId=t2.accountId and main.flowId=t2.flowId");
        tableEnv.registerTable("corpusCountInfo", corpusCountInfo);
//        tableEnv.toDataSet(corpusCountInfo, Row.class).print("corpusCountInfo");




        if(Constants.DBType.MYSQL.getValue().equals(dbType)){
            // 最终结果
            Table statisticsResult = tableEnv.sqlQuery("select DISTINCT " +
                    "concat(COALESCE(main.dayStr, ''),main.accountId,main.flowId) as id," +
                    "main.dayStr," +
                    "main.accountId," +
                    "info.robotId," +
                    "info.title as flowName," +
                    "COALESCE(main.cnt, 0) as cnt," +
                    "COALESCE(main.splitCnt, 0) as ins," +
                    "COALESCE(main.markCnt, 0) as markCnt," +
                    "COALESCE(main.ins, 0) as splitCnt," +
                    "CAST(main.dayStr AS TIMESTAMP) as dayt " +
                    "from" +
                    "(select DISTINCT dayStr, accountId, flowId, sum(ins) as ins, sum(splitCnt) as splitCnt, sum(cnt) as cnt, sum(markCnt) as markCnt " +
                    "   from (select * from corpusCountInfo UNION select * from markCnt) c group by dayStr, accountId, flowId) main " +
                    "inner join " +
                    "(select distinct flowId, accountId, robotId, title from corpus) info on main.accountId=info.accountId and main.flowId=info.flowId " +
                    "where concat(COALESCE(main.dayStr, ''),main.accountId,main.flowId) is not null ");
            tableEnv.toDataSet(statisticsResult, Row.class).print("statisticsResult");
            String sinkQuery = "REPLACE INTO stat_model_train(id, day_str, account_id, robot_id, words_name, knowledge_cnt, words_add_cnt, mark_cnt, intention_add_cnt, day) values(?,?,?,?,?,?,?,?,?,?)";
            // 输出结果
            JDBCAppendTableSink tableSink = new JDBCAppendTableSinkBuilder()
                    .setDBUrl(dbUrl)
                    .setDrivername(dbDriver)
                    .setUsername(dbUser)
                    .setPassword(dbPass)
                    .setBatchSize(1000)
                    .setQuery(sinkQuery)
                    .setParameterTypes(new TypeInformation[]{Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.INT, Types.INT, Types.INT, Types.INT, Types.SQL_TIMESTAMP})
                    .build();
            tableEnv.registerTableSink("Result",
                    new String[]{"id", "dayStr", "accountId", "robotId", "flowName", "cnt", "ins", "markCnt", "splitCnt", "dayt"},
                    new TypeInformation[]{Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.INT, Types.INT, Types.INT, Types.INT, Types.SQL_TIMESTAMP},
                    tableSink);
            statisticsResult.insertInto("Result");
        } else if (Constants.DBType.ORACLE.getValue().equals(dbType)) {
            // 最终结果
            Table statisticsResult = tableEnv.sqlQuery("select DISTINCT " +
                    "concat(COALESCE(main.dayStr, ''),main.accountId,main.flowId) as qid," +
                    "COALESCE(main.cnt, 0) as qcnt," +
                    "COALESCE(main.splitCnt, 0) as qins," +
                    "COALESCE(main.markCnt, 0) as qmarkCnt," +
                    "COALESCE(main.ins, 0) as qsplitCnt," +
                    "concat(COALESCE(main.dayStr, ''),main.accountId,main.flowId) as id," +
                    "main.dayStr," +
                    "main.accountId," +
                    "info.robotId," +
                    "info.title as flowName," +
                    "COALESCE(main.cnt, 0) as cnt," +
                    "COALESCE(main.splitCnt, 0) as ins," +
                    "COALESCE(main.markCnt, 0) as markCnt," +
                    "COALESCE(main.ins, 0) as splitCnt," +
                    "CAST(main.dayStr AS TIMESTAMP) as dayt " +
                    "from" +
                    "(select DISTINCT dayStr, accountId, flowId, sum(ins) as ins, sum(splitCnt) as splitCnt, sum(cnt) as cnt, sum(markCnt) as markCnt " +
                    "   from (select * from corpusCountInfo UNION select * from markCnt) c group by dayStr, accountId, flowId) main " +
                    "inner join " +
                    "(select distinct flowId, accountId, robotId, title from corpus) info on main.accountId=info.accountId and main.flowId=info.flowId " +
                    "where concat(COALESCE(main.dayStr, ''),main.accountId,main.flowId) is not null ");
            tableEnv.toDataSet(statisticsResult, Row.class).print("statisticsResult");
            String sinkQuery = "MERGE INTO stat_model_train T1 " +
                    "USING (SELECT ? AS ID FROM dual) T2 " +
                    "ON (T1.ID=T2.ID) " +
                    "WHEN MATCHED THEN " +
                    "UPDATE SET T1.knowledge_cnt=?, T1.words_add_cnt=?, T1.mark_cnt=?, T1.intention_add_cnt=? " +
                    "WHEN NOT MATCHED THEN " +
                    "INSERT (id, day_str, account_id, robot_id, words_name, knowledge_cnt, words_add_cnt, mark_cnt, intention_add_cnt, day) VALUES(?,?,?,?,?,?,?,?,?,?)";
            // 输出结果
            JDBCAppendTableSink tableSink = new JDBCAppendTableSinkBuilder()
                    .setDBUrl(dbUrl)
                    .setDrivername(dbDriver)
                    .setUsername(dbUser)
                    .setPassword(dbPass)
                    .setBatchSize(1000)
                    .setQuery(sinkQuery)
                    .setParameterTypes(new TypeInformation[]{Types.STRING, Types.INT, Types.INT, Types.INT, Types.INT, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.INT, Types.INT, Types.INT, Types.INT, Types.SQL_TIMESTAMP})
                    .build();
            tableEnv.registerTableSink("Result",
                    new String[]{"qid", "qcnt", "qins", "qmarkCnt", "qsplitCnt", "id", "dayStr", "accountId", "robotId", "flowName", "cnt", "ins", "markCnt", "splitCnt", "dayt"},
                    new TypeInformation[]{Types.STRING, Types.INT, Types.INT, Types.INT, Types.INT, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.INT, Types.INT, Types.INT, Types.INT, Types.SQL_TIMESTAMP},
                    tableSink);
            statisticsResult.insertInto("Result");
        }
        env.execute(jobName);
    }
}
