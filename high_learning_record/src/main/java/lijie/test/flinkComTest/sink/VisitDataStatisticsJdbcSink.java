package lijie.test.flinkComTest.sink;

import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.VisitDataStatisticsOutput;
import lijie.test.flinkComTest.util.DbUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class VisitDataStatisticsJdbcSink implements SinkFunction<VisitDataStatisticsOutput> {

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public VisitDataStatisticsJdbcSink(String dbDriver, String dbUrl, String dbUser, String dbPass) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    @Override
    public void invoke(VisitDataStatisticsOutput value, Context context) throws Exception {
        Connection conn = DbUtil.openSimpleDbConnection(dbDriver, dbUrl, dbUser, dbPass);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO " +
                "stat_visit_base(day_str, visit_cnt, action_cnt, action_avg_cnt, action_avg_duration, robot_cnt, day, sys_user_id, sys_company_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?)");
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE " +
                "stat_visit_base SET visit_cnt = ?, action_cnt = ?, action_avg_cnt = ?, action_avg_duration = ?, robot_cnt = ? " +
                "WHERE day_str = ? AND sys_user_id = ? AND sys_company_id = ?");
        // 执行更新语句
        updateStmt.setLong(1, value.getVisitCnt());
        updateStmt.setLong(2, value.getActionCnt());
        updateStmt.setLong(3, value.getActionAvgCnt());
        updateStmt.setLong(4, value.getActionAvgDuration());
        updateStmt.setLong(5, value.getRobotCnt());
        updateStmt.setString(6, value.getDayStr());
        updateStmt.setString(7, value.getSysUserId());
        updateStmt.setString(8, value.getSysCompanyId());
        updateStmt.execute();
        // 如果update没有查到数据，那么执行插入语句
        if(updateStmt.getUpdateCount() == 0){
            insertStmt.setString(1, value.getDayStr());
            insertStmt.setLong(2, value.getVisitCnt());
            insertStmt.setLong(3, value.getActionCnt());
            insertStmt.setLong(4, value.getActionAvgCnt());
            insertStmt.setLong(5, value.getActionAvgDuration());
            insertStmt.setLong(6, value.getRobotCnt());
            insertStmt.setDate(7, new Date(new SimpleDateFormat(Constants.dayDateFormatStr).parse(value.getDayStr()).getTime()));
            insertStmt.setString(8, value.getSysUserId());
            insertStmt.setString(9, value.getSysCompanyId());
            insertStmt.execute();
        }
        DbUtil.closeSimpleDbConnection(conn);
    }

}
