package lijie.test.flinkComTest.entity.kafka;


import lijie.test.flinkComTest.entity.CallStatistics;
import lombok.Data;

import java.util.Date;

@Data
public class ConversationInfo extends CallStatistics {

    // 频道
    private String channel;

    // 机器人ID
    private String robotId;

    // 话术名称
    private String wordsName;

    // 会话ID
    private String sessionId;

    // 用户ID
    private String userId;

    // 子账号
    private String sysUserId;

    // 企业主账号
    private String sysCompanyId;

    // 用户输入
    private String userInput;

    // 用户输入标准问
    private String standardInput;

    // 机器人输出
    private String robotOutput;

    // 用户标签
    private String userLabel;

    // 节点标签
    private String nodeLabel;

    // 最像的关键词
    private String keyword;

    // 消息时间
    private Date time;

    // 消息日期字符串
    private String dayStr;

}
