package lijie.test.flinkComTest.util;

import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * flink作业工具
 *
 * @author xuhongchen
 */
public class ProcessUtil {

    public static JDBCInputFormat buildJDBCInputFormat(String driverName, String url, String user, String password, String sql, RowTypeInfo infoTypeInfo) {

        return JDBCInputFormat.buildJDBCInputFormat()
                .setDrivername(driverName)
                .setDBUrl(url)
                .setUsername(user)
                .setPassword(password)
                .setQuery(sql)
                .setRowTypeInfo(infoTypeInfo)
                .finish();
    }

    /**
     * 作业参数检查
     *
     * @param args
     * @param properties
     * @param logger
     * @return
     * @throws IOException
     */
    public static boolean jobArgsCheck(String[] args, Properties properties, Logger logger) throws IOException {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String configPath = parameterTool.get("config");
        if (StringUtils.isNullOrWhitespaceOnly(configPath)) {
            System.out.println("Missing config!\n");
            logger.error("Missing config!\n");
            return false;
        }

//        InputStream in = ConversationRealTimeStatistics.class.getClassLoader().getResourceAsStream("config.properties");
        InputStream in = new BufferedInputStream(new FileInputStream(configPath));
        if (null == in) {
            System.out.println("Missing config!\n");
            logger.error("Missing config!\n");
            return false;
        }
        properties.load(in);
        return true;
    }

    /**
     * 数据库配置检查
     * @param dbDriver
     * @param dbUrl
     * @param dbUser
     * @param dbPass
     * @return
     */
    public static boolean dbParamCheck(String dbDriver, String dbUrl, String dbUser, String dbPass) {
        if (StringUtils.isNullOrWhitespaceOnly(dbDriver)) {
            System.out.println("Missing fansRealTimeStatistics.dbDriver!\n");
            return true;
        }
        if (StringUtils.isNullOrWhitespaceOnly(dbUrl)) {
            System.out.println("Missing fansRealTimeStatistics.dbUrl!\n");
            return true;
        }
        if (StringUtils.isNullOrWhitespaceOnly(dbUser)) {
            System.out.println("Missing fansRealTimeStatistics.dbUser!\n");
            return true;
        }
        if (StringUtils.isNullOrWhitespaceOnly(dbPass)) {
            System.out.println("Missing fansRealTimeStatistics.dbPass!\n");
            return true;
        }
        return false;
    }

}
