package lijie.test.flinkComTest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.typeutils.RowTypeInfo;

@Data
@ToString
@NoArgsConstructor
public class BatchTableNameInfo {

    private String username;

    private String flowId;
    
    private String flowName;

    private String robotId;

    private String accountId;

    public static RowTypeInfo buildRowTypeInfo(){
        return new RowTypeInfo(
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO);
    }

}