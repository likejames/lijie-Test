package lijie.test.flinkComTest.batch.sql;

import org.apache.flink.table.functions.TableFunction;

public class SplitStringTable extends TableFunction<String> {
    private String separator = " ";

    public SplitStringTable(String separator) {
        this.separator = separator;
    }

    public void eval(String str) {
        for (String s : str.split(separator)) {
            collect(s);
        }
    }

}
