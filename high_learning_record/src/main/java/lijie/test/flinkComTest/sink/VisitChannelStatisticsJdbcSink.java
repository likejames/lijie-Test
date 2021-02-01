package lijie.test.flinkComTest.sink;


import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.VisitChannelStatisticsOutput;
import lijie.test.flinkComTest.util.DbUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class VisitChannelStatisticsJdbcSink implements SinkFunction<VisitChannelStatisticsOutput> {

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public VisitChannelStatisticsJdbcSink(String dbDriver, String dbUrl, String dbUser, String dbPass) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    @Override
    public void invoke(VisitChannelStatisticsOutput value, Context context) throws Exception {
        Connection conn = DbUtil.openSimpleDbConnection(dbDriver, dbUrl, dbUser, dbPass);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO " +
                "stat_visit_channel(day_str, channel, visit_cnt, day, sys_user_id, sys_company_id) " +
                "VALUES (?,?,?,?,?,?)");
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE " +
                "stat_visit_channel SET visit_cnt=? " +
                "WHERE day_str = ? AND sys_user_id = ? AND sys_company_id = ? AND channel = ?");
        // 执行更新语句
        updateStmt.setLong(1, value.getVisitCnt());
        updateStmt.setString(2, value.getDayStr());
        updateStmt.setString(3, value.getSysUserId());
        updateStmt.setString(4, value.getSysCompanyId());
        updateStmt.setString(5, value.getChannel());
        updateStmt.execute();
        // 如果update没有查到数据，那么执行插入语句
        if(updateStmt.getUpdateCount() == 0){
            insertStmt.setString(1, value.getDayStr());
            insertStmt.setString(2, value.getChannel());
            insertStmt.setLong(3, value.getVisitCnt());
            insertStmt.setDate(4, new Date(new SimpleDateFormat(Constants.dayDateFormatStr).parse(value.getDayStr()).getTime()));
            insertStmt.setString(5, value.getSysUserId());
            insertStmt.setString(6, value.getSysCompanyId());
            insertStmt.execute();
        }
        DbUtil.closeSimpleDbConnection(conn);
    }
}
