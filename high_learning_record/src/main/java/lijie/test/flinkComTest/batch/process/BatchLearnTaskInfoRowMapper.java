package lijie.test.flinkComTest.batch.process;


import lijie.test.flinkComTest.entity.BatchTableLearnTaskInfo;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.types.Row;

import java.sql.Timestamp;

public class BatchLearnTaskInfoRowMapper extends RichMapFunction<Row, BatchTableLearnTaskInfo> {
    @Override
    public BatchTableLearnTaskInfo map(Row row) throws Exception {
        BatchTableLearnTaskInfo report = new BatchTableLearnTaskInfo();
        report.setUsername(null == row.getField(0) ? null : row.getField(0).toString());
        report.setFlowId(null == row.getField(1) ? null : row.getField(1).toString());
        report.setFlowName(null == row.getField(2) ? null : row.getField(2).toString());
        report.setRobotId(null == row.getField(3) ? null : row.getField(3).toString());
        report.setOperator(null == row.getField(4) ? null : row.getField(4).toString());
        report.setAccountId(null == row.getField(5) ? null : row.getField(5).toString());
        report.setMarkTime(null == row.getField(6) ? null : Timestamp.valueOf(row.getField(6).toString()));
        report.setUserId(null == row.getField(7) ? null : row.getField(7).toString());
        return report;
    }
}
