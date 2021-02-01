package lijie.test.flinkComTest.sink;

import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.ServiceRateStatisticsOutput;
import lijie.test.flinkComTest.util.DbUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class ServiceRateStatisticsJdbcSink implements SinkFunction<ServiceRateStatisticsOutput> {

    private String dbDriver;
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public ServiceRateStatisticsJdbcSink(String dbDriver, String dbUrl, String dbUser, String dbPass) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    @Override
    public void invoke(ServiceRateStatisticsOutput value, Context context) throws Exception {
        Connection conn = DbUtil.openSimpleDbConnection(dbDriver, dbUrl, dbUser, dbPass);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO " +
                "stat_service_rate_metric(day_str, robot_id, words_name, reply, cnt, day, sys_user_id, sys_company_id) " +
                "VALUES (?,?,?,?,?,?,?,?)");
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE stat_service_rate_metric SET cnt = ? " +
                "WHERE day_str = ? AND sys_user_id = ? AND sys_company_id = ? AND robot_id = ? AND reply = ?");
        // 执行更新语句
        updateStmt.setLong(1, value.getCnt());
        updateStmt.setString(2, value.getDayStr());
        updateStmt.setString(3, value.getSysUserId());
        updateStmt.setString(4, value.getSysCompanyId());
        updateStmt.setString(5, value.getRobotId());
        updateStmt.setString(6, value.getReply());
        updateStmt.execute();
        // 如果update没有查到数据，那么执行插入语句
        if(updateStmt.getUpdateCount() == 0){
            insertStmt.setString(1, value.getDayStr());
            insertStmt.setString(2, value.getRobotId());
            insertStmt.setString(3, value.getWordsName());
            insertStmt.setString(4, value.getReply());
            insertStmt.setLong(5, value.getCnt());
            insertStmt.setDate(6, new Date(new SimpleDateFormat(Constants.dayDateFormatStr).parse(value.getDayStr()).getTime()));
            insertStmt.setString(7, value.getSysUserId());
            insertStmt.setString(8, value.getSysCompanyId());
            insertStmt.execute();
        }
        DbUtil.closeSimpleDbConnection(conn);
    }

}
