package lijie.test.flinkComTest.batch.sql;

import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;

public class TimestampIsNotNull extends ScalarFunction {

    public boolean eval(Timestamp date){
        return null == date ? false : true;
    }
}
