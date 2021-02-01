package lijie.test.flinkComTest.streaming.process;

import lijie.test.flinkComTest.entity.HotIssueStatisticsOutput;
import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class HotIssueCountWindowProcess extends KeyedProcessFunction<Tuple, ConversationInfo, HotIssueStatisticsOutput> {

    /**
     * 触发次数状态
     *
     */
    private ValueState<Long> countStatus;

    @Override
    public void open(Configuration parameters) throws Exception {
        countStatus = getRuntimeContext().getState(new ValueStateDescriptor<Long>("countStatus", Long.class));
    }

    @Override
    public void processElement(ConversationInfo value, Context ctx, Collector<HotIssueStatisticsOutput> out) throws Exception {
        // 日期
        String day = ctx.getCurrentKey().getField(0);
        // 主账号
        String sysCompanyId = ctx.getCurrentKey().getField(1);
        // 子账号
        String sysUserId = ctx.getCurrentKey().getField(2);
        // 机器人标识
        String robotId = ctx.getCurrentKey().getField(3);
        // 标准问
        String issue = ctx.getCurrentKey().getField(4);
        // 意图
        String intention = ctx.getCurrentKey().getField(5);
        // 触发次数
        Long preCnt = null == countStatus.value() ? 0L : countStatus.value();
        String wordsName = value.getWordsName();
        countStatus.update(preCnt + 1);
        Long cnt = countStatus.value();
        if(cnt > 0){
            HotIssueStatisticsOutput output = new HotIssueStatisticsOutput(robotId, wordsName, issue, intention, cnt, day, sysUserId, sysCompanyId);
            out.collect(output);
        }
    }
}
