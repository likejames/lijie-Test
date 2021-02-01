package lijie.test.flinkComTest.streaming.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import lijie.test.flinkComTest.util.DbUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class KafkaMsgConverter implements MapFunction<String, ConversationInfo> {

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    private String flowDbDriver;
    private String flowDbUrl;
    private String flowDbUser;
    private String flowDbPass;

    public KafkaMsgConverter(String dbDriver, String dbUrl, String dbUser, String dbPass, String flowDbDriver, String flowDbUrl, String flowDbUser, String flowDbPass) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.flowDbDriver = flowDbDriver;
        this.flowDbUrl = flowDbUrl;
        this.flowDbUser = flowDbUser;
        this.flowDbPass = flowDbPass;
    }

    @Override
    public ConversationInfo map(String value) throws Exception {
        JSONObject jsonObject = JSON.parseObject(value);
        ConversationInfo result = new ConversationInfo();
        // 通道
        result.setChannel(jsonObject.getString("channel"));
        // 机器人标识
        result.setRobotId(jsonObject.getString("robotId"));
        // 话术名称
        result.setWordsName(jsonObject.getString("robotName"));
        // 会话标识
        result.setSessionId(jsonObject.getString("sessionId"));
        // 消息用户标识
        result.setUserId(jsonObject.getString("userId"));
        // 用户输入
        result.setUserInput(jsonObject.getString("userInput"));
        // 标准问
        result.setStandardInput(jsonObject.getString("interactAsk"));
        // 机器人输出
        result.setRobotOutput(jsonObject.getString("robotOutput"));
        // 用户标签
        result.setUserLabel(jsonObject.getString("userLabel"));
        // 节点标签
        if(StringUtils.isNullOrWhitespaceOnly(jsonObject.getString("nodeLabel")) ||
                "None".equals(jsonObject.getString("nodeLabel"))){
            result.setNodeLabel("未知");
        } else {
            result.setNodeLabel(jsonObject.getString("nodeLabel"));
        }
        // 关键字
        result.setKeyword(jsonObject.getString("mostLike"));
        // 时间
        String rowTimeStr = jsonObject.getString("time");
        if(!StringUtils.isNullOrWhitespaceOnly(rowTimeStr)){
            if(rowTimeStr.contains(",")){
                result.setTime(new SimpleDateFormat(Constants.fullDateFormatStr).parse(rowTimeStr.substring(0, rowTimeStr.indexOf(","))));
            } else if(rowTimeStr.contains(":")){
                result.setTime(new SimpleDateFormat(Constants.fullDateFormatStr).parse(rowTimeStr));
            } else {
                result.setTime(new SimpleDateFormat(Constants.dayDateFormatStr).parse(rowTimeStr));
            }
            String dayStr = new SimpleDateFormat(Constants.dayDateFormatStr).format(result.getTime());
            result.setDayStr(dayStr);
        }

        if(!StringUtils.isNullOrWhitespaceOnly(result.getRobotId())){
            String aId = null;
            // 获得创建机器人的流程账号
            Connection flowCon = DbUtil.openSimpleDbConnection(flowDbDriver, flowDbUrl, flowDbUser, flowDbPass);
            PreparedStatement flowPs = flowCon.prepareStatement("SELECT id, account_id FROM corpus_robot WHERE id=?");
            flowPs.setString(1, result.getRobotId());
            ResultSet flowRs = flowPs.executeQuery();
            while (flowRs.next()){
                aId = flowRs.getString("account_id");
            }
            if(!StringUtils.isNullOrWhitespaceOnly(aId)){
                // 获取流程账号对应的用户id
                Connection connection = DbUtil.openSimpleDbConnection(dbDriver, dbUrl, dbUser, dbPass);
                PreparedStatement ps = connection.prepareStatement("SELECT id, CASE is_enterprise_user WHEN 1 THEN enterprise_user_id ELSE id END AS cid, flow_user_id FROM sys_user WHERE flow_user_id=?");
                ps.setString(1, aId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    result.setSysUserId(rs.getString("id"));
                    result.setSysCompanyId(rs.getString("cid"));
                }
                if (ps != null) {
                    ps.close();
                }
                DbUtil.closeSimpleDbConnection(connection);
            }
            if (flowPs != null) {
                flowPs.close();
            }
            DbUtil.closeSimpleDbConnection(flowCon);
        }
        return result;
    }
}
