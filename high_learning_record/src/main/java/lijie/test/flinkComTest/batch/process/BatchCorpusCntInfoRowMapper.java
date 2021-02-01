package lijie.test.flinkComTest.batch.process;

import lijie.test.flinkComTest.entity.BatchTableCorpusTotalCntInfo;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.types.Row;

public class BatchCorpusCntInfoRowMapper extends RichMapFunction<Row, BatchTableCorpusTotalCntInfo> {
    @Override
    public BatchTableCorpusTotalCntInfo map(Row row) throws Exception {
        BatchTableCorpusTotalCntInfo report = new BatchTableCorpusTotalCntInfo();
        report.setAccountId(null == row.getField(0) ? null : row.getField(0).toString());
        report.setFlowId(null == row.getField(1) ? null : row.getField(1).toString());
        report.setCnt(null == row.getField(2) ? null : Integer.parseInt(row.getField(2).toString()));
        return report;
    }
}
