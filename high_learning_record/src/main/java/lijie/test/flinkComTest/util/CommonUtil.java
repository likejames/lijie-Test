package lijie.test.flinkComTest.util;

import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.http.RobotRestApiService;
import org.apache.flink.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 公共工具类
 *
 * @author xuhongchen
 */
public class CommonUtil {

    /**
     * 通过生日获得年龄
     *
     * @param birthday
     * @return
     */
    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            // 当前时间
            now.setTime(new Date());
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);
            // 如果传入的时间，在当前时间的后面，返回0岁
            if (birth.after(now)) {
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获得特定时候与当前时间的间隔天数
     *
     * @param date
     * @return
     */
    public static Integer diffDayToNow(Date date) {
        if (null == date) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        if (start.after(end)) {
            return null;
        }
        long n = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (n / 60 * 60 * 24 * 1000L);
    }

    /**
     * 获取指定时间的前一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getDayBefore(String specifiedDay) {
        return getString(specifiedDay, -1);
    }

    /**
     * 获取指定时间的某一天
     *
     * @param specifiedDay
     * @param i
     * @return
     */
    public static String getString(String specifiedDay, int i) {
        if (StringUtils.isNullOrWhitespaceOnly(specifiedDay)) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(Constants.dayDateFormatStr).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.DATE, i);
        String dayBefore = new SimpleDateFormat(Constants.dayDateFormatStr).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获取指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getDayAfter(String specifiedDay) {
        return getString(specifiedDay, 1);
    }

    /**
     * 获取指定日期间隔的日期序列
     *
     * @param startDay
     * @param endDay
     * @return
     */
    public static ArrayList<String> getSeqBetween(Date startDay, Date endDay, String dateFormatStr) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(dateFormatStr);
        ArrayList<String> seq = new ArrayList<String>();
        Date temp = startDay;
        int len = 86400000;
        while(temp.before(endDay)){
            seq.add(dayFormat.format(temp));
            long timestamp = temp.getTime();
            timestamp += len;
            temp = new Date(timestamp);
        }
        seq.add(dayFormat.format(endDay));
        return seq;
    }

    /**
     * 中文转unicode码
     *
     * @param str
     * @return
     */
    public static String gbEncoding(final String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            // 汉字范围 \u4e00-\u9fa5 (中文)
            if (chr1 >= 19968 && chr1 <= 171941) {
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }

    /**
     * 时间戳转换为时间
     *
     * @param l
     * @return
     */
    public static Date getDateByTimeStamp(long l) {
        long msl = l * 1000;
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = null;
        try {
            String str = sim.format(msl);
            dateTime = sim.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * 测试方法
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

//        String result = CommonUtil.gbEncoding("你好吗，好久不见aa");
//
//        System.out.println(result);


        RobotRestApiService service = new RobotRestApiService("114.116.108.29:5080", "/robot/search.do", "265bba00e49e9e2a7b2cc18930ba1ebb");

        Long robotCnt = service.getTotalRobotCnt();

        System.out.println(robotCnt);



    }

}
