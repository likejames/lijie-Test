package lijie.test.flinkComTest.sink;


import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.HotIssueStatisticsOutput;
import lijie.test.flinkComTest.util.DbUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class HotIssueStatisticsJdbcSink implements SinkFunction<HotIssueStatisticsOutput> {

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public HotIssueStatisticsJdbcSink(String dbDriver, String dbUrl, String dbUser, String dbPass) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    @Override
    public void invoke(HotIssueStatisticsOutput value, Context context) throws Exception {
        Connection conn = DbUtil.openSimpleDbConnection(dbDriver, dbUrl, dbUser, dbPass);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO " +
                "stat_hot_issue(day_str, robot_id, words_name, issue, intention, cnt, day, sys_user_id, sys_company_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?)");
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE stat_hot_issue SET cnt = ? " +
                "WHERE day_str = ? AND sys_user_id = ? AND sys_company_id = ? AND robot_id = ? AND issue = ? AND intention = ?");
        // 执行更新语句
        updateStmt.setLong(1, value.getCnt());
        updateStmt.setString(2, value.getDayStr());
        updateStmt.setString(3, value.getSysUserId());
        updateStmt.setString(4, value.getSysCompanyId());
        updateStmt.setString(5, value.getRobotId());
        updateStmt.setString(6, value.getIssue());
        updateStmt.setString(7, value.getIntention());
        updateStmt.execute();
        // 如果update没有查到数据，那么执行插入语句
        if(updateStmt.getUpdateCount() == 0){
            insertStmt.setString(1, value.getDayStr());
            insertStmt.setString(2, value.getRobotId());
            insertStmt.setString(3, value.getWordsName());
            insertStmt.setString(4, value.getIssue());
            insertStmt.setString(5, value.getIntention());
            insertStmt.setLong(6, value.getCnt());
            insertStmt.setDate(7, new Date(new SimpleDateFormat(Constants.dayDateFormatStr).parse(value.getDayStr()).getTime()));
            insertStmt.setString(8, value.getSysUserId());
            insertStmt.setString(9, value.getSysCompanyId());
            insertStmt.execute();
        }
        DbUtil.closeSimpleDbConnection(conn);
    }
}
