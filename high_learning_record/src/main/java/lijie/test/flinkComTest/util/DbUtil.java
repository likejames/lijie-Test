package lijie.test.flinkComTest.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库相关工具类
 *
 * @author xuhongchen
 */
public class DbUtil {

    public static Connection openSimpleDbConnection(String dbDriver, String dbUrl, String dbUser, String dbPass) throws Exception {
        Class.forName(dbDriver);
        return DriverManager.getConnection(dbUrl,dbUser,dbPass);
    }

    public static void closeSimpleDbConnection(Connection conn) throws Exception {
        if(null != conn){
            conn.close();
        }
    }

}
