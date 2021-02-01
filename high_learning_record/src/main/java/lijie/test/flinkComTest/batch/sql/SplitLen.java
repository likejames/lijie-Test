package lijie.test.flinkComTest.batch.sql;

import org.apache.flink.table.functions.ScalarFunction;

public class SplitLen extends ScalarFunction {

    private String separator = " ";

    public SplitLen(String separator) {
        this.separator = separator;
    }

    public Integer eval(String content){
        return content.split(separator).length;
    }
}
