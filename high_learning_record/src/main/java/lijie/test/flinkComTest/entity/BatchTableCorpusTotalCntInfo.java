package lijie.test.flinkComTest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.typeutils.RowTypeInfo;

@Data
@ToString
@NoArgsConstructor
public class BatchTableCorpusTotalCntInfo {

    // 流程id
    private String flowId;
    // 账号
    private String accountId;
    // 知识库数
    private Integer cnt;
    
    public static RowTypeInfo buildRowTypeInfo(){
        return new RowTypeInfo(
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.LONG_TYPE_INFO);
    }

}