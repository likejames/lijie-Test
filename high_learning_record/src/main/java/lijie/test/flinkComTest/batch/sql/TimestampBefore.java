package lijie.test.flinkComTest.batch.sql;


import lijie.test.flinkComTest.common.Constants;
import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimestampBefore extends ScalarFunction {

    private static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(Constants.dayDateFormatStr));

    public Long eval(Timestamp date, String other) throws ParseException {

        java.util.Date javaDate = dateFormat.get().parse(other);
        System.out.println(javaDate);
        return date.before(new Timestamp(javaDate.getTime())) ? 1L : 0L;
    }
}
