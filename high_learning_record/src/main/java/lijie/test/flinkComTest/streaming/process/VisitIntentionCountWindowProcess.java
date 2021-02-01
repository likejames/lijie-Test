package lijie.test.flinkComTest.streaming.process;


import lijie.test.flinkComTest.entity.VisitIntentionStatisticsOutput;
import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.calcite.shaded.com.google.common.collect.Lists;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class VisitIntentionCountWindowProcess extends KeyedProcessFunction<Tuple, ConversationInfo, VisitIntentionStatisticsOutput> {
    /**
     * 会话id列表
     *
     */
    private ListState<String> sessionIdsStatus;

    @Override
    public void open(Configuration parameters) throws Exception {
        sessionIdsStatus = getRuntimeContext().getListState(new ListStateDescriptor<String>("sessionIdsStatus", String.class));
    }

    @Override
    public void processElement(ConversationInfo value, Context ctx, Collector<VisitIntentionStatisticsOutput> out) throws Exception {
        // 日期
        String day = ctx.getCurrentKey().getField(0);
        // 主账号
        String sysCompanyId = ctx.getCurrentKey().getField(1);
        // 子账号
        String sysUserId = ctx.getCurrentKey().getField(2);
        // 意图
        String intention = ctx.getCurrentKey().getField(3);
        if(!Lists.newArrayList(sessionIdsStatus.get()).contains(value.getSessionId())){
            sessionIdsStatus.add(value.getSessionId());
        }
        Integer visitCnt = Lists.newArrayList(sessionIdsStatus.get()).size();
        VisitIntentionStatisticsOutput output = new VisitIntentionStatisticsOutput(intention, visitCnt.longValue(), day, sysUserId, sysCompanyId);
        out.collect(output);
    }
}
