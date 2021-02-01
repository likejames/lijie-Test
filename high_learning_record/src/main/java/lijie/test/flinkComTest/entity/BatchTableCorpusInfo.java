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
public class BatchTableCorpusInfo {

    // 语料内容
    private String content;
    // 流程id
    private String flowId;
    // 账号
    private String accountId;
    // 语料创建时间
    private Timestamp createTimestamp;
    // 机器人id
    private String robotId;
    // 机器人名称
    private String title;

    public static RowTypeInfo buildRowTypeInfo(){
        return new RowTypeInfo(
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO,
                Types.SQL_TIMESTAMP,
                BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO);
    }

}