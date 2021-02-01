package lijie.test.flinkComTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class VisitDataStatisticsOutput extends CallStatistics {
    // 数据访问数
    private Long visitCnt = 0L;
    // 累计互动次数
    private Long actionCnt = 0L;
    // 平均互动轮次
    private Long actionAvgCnt = 0L;
    // 平均互动时长
    private Long actionAvgDuration = 0L;
    // 机器人（话术）数量
    private Long robotCnt = 0L;
    // 机器人（话术）总数
    private Long robotTotalCnt = 0L;
    // 消息日期字符串
    private String dayStr;
    // 子账号
    private String sysUserId;
    // 企业主账号
    private String sysCompanyId;
}
