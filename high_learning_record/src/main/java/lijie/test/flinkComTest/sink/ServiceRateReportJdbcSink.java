package lijie.test.flinkComTest.sink;


import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import lijie.test.flinkComTest.util.DbUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ServiceRateReportJdbcSink implements SinkFunction<ConversationInfo> {

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public ServiceRateReportJdbcSink(String dbDriver, String dbUrl, String dbUser, String dbPass) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    @Override
    public void invoke(ConversationInfo value, Context context) throws Exception {
        Connection conn = DbUtil.openSimpleDbConnection(dbDriver, dbUrl, dbUser, dbPass);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO " +
                "stat_service_rate_report(issue, reply, robot_id, words_name, session_time, sys_user_id, sys_company_id) " +
                "VALUES (?,?,?,?,?,?,?)");
        insertStmt.setString(1, value.getUserInput());
        insertStmt.setString(2, value.getNodeLabel());
        insertStmt.setString(3, value.getRobotId());
        insertStmt.setString(4, value.getWordsName());
        insertStmt.setTimestamp(5, new java.sql.Timestamp(value.getTime().getTime()));
        insertStmt.setString(6, value.getSysUserId());
        insertStmt.setString(7, value.getSysCompanyId());
        insertStmt.execute();
        DbUtil.closeSimpleDbConnection(conn);
    }

}
