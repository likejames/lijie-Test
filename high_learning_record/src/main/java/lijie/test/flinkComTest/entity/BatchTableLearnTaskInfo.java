package lijie.test.flinkComTest.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
public class BatchTableLearnTaskInfo {

    private String username;

    private String flowId;
    
    private String flowName;
    
    private String robotId;

    private String operator;
    
    private String accountId;

    private Timestamp markTime;

    private String userId;
    
    public static RowTypeInfo buildRowTypeInfo(){
        return new RowTypeInfo(
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                Types.SQL_TIMESTAMP,
                BasicTypeInfo.STRING_TYPE_INFO);
    }

}