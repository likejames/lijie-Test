package lijie.test.flinkComTest.batch.process;

import lijie.test.flinkComTest.entity.BatchTableNameInfo;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.types.Row;

public class BatchNameInfoRowMapper extends RichMapFunction<Row, BatchTableNameInfo> {
    @Override
    public BatchTableNameInfo map(Row row) throws Exception {
        BatchTableNameInfo report = new BatchTableNameInfo();
        report.setUsername(null == row.getField(0) ? null : row.getField(0).toString());
        report.setFlowId(null == row.getField(1) ? null : row.getField(1).toString());
        report.setFlowName(null == row.getField(2) ? null : row.getField(2).toString());
        report.setRobotId(null == row.getField(3) ? null : row.getField(3).toString());
        report.setAccountId(null == row.getField(4) ? null : row.getField(4).toString());
        return report;
    }
}
