package lijie.test.flinkComTest.batch.sql;

import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.util.StringUtils;

public class StringToZeroOrOne extends ScalarFunction {

    public int eval(String str){
        return StringUtils.isNullOrWhitespaceOnly(str) ? 0 : 1;
    }

}
