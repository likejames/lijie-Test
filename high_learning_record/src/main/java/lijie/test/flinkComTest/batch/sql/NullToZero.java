package lijie.test.flinkComTest.batch.sql;

import org.apache.flink.table.functions.ScalarFunction;

public class NullToZero extends ScalarFunction {

    public Long eval(Long num){
        return null == num ? 0 : num;
    }

}
