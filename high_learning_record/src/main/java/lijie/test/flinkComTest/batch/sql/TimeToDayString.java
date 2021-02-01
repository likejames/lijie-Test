package lijie.test.flinkComTest.batch.sql;


import lijie.test.flinkComTest.common.Constants;
import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeToDayString extends ScalarFunction {

    private static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(Constants.dayDateFormatStr));

    public String eval(Timestamp date){
        Long time = date.getTime();
        java.util.Date javaDate = new java.util.Date(time);
        return dateFormat.get().format(javaDate);
    }

}
