package lijie.test.flinkComTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class HotIssueStatisticsOutput extends CallStatistics {
    // 机器人id
    private String robotId;
    // 话术名称
    private String wordsName;
    // 问题
    private String issue;
    // 意图类别
    private String intention;
    // 触发次数
    private Long cnt = 0L;
    // 消息日期字符串
    private String dayStr;
    // 子账号
    private String sysUserId;
    // 企业主账号
    private String sysCompanyId;
}
