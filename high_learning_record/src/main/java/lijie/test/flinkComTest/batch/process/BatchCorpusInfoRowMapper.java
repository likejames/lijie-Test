package lijie.test.flinkComTest.batch.process;


import lijie.test.flinkComTest.entity.BatchTableCorpusInfo;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.types.Row;

import java.sql.Timestamp;

public class BatchCorpusInfoRowMapper extends RichMapFunction<Row, BatchTableCorpusInfo> {

    @Override
    public BatchTableCorpusInfo map(Row row) throws Exception {
        BatchTableCorpusInfo report = new BatchTableCorpusInfo();
        report.setContent(null == row.getField(0) ? null : row.getField(0).toString());
        report.setFlowId(null == row.getField(1) ? null : row.getField(1).toString());
        report.setAccountId(null == row.getField(2) ? null : row.getField(2).toString());
        report.setCreateTimestamp(null == row.getField(3) ? null : Timestamp.valueOf(row.getField(3).toString()));
        report.setRobotId(null == row.getField(4) ? null : row.getField(4).toString());
        report.setTitle(null == row.getField(5) ? null : row.getField(5).toString());
        return report;
    }
}
