package lijie.test.flinkComTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class VisitChannelStatisticsOutput extends CallStatistics {
    // 渠道
    private String channel;
    // 数据访问数
    private Long visitCnt = 0L;
    // 消息日期字符串
    private String dayStr;
    // 子账号
    private String sysUserId;
    // 企业主账号
    private String sysCompanyId;

}
