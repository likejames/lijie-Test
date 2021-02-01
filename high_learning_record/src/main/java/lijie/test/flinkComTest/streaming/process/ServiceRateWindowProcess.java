package lijie.test.flinkComTest.streaming.process;


import lijie.test.flinkComTest.common.Constants;
import lijie.test.flinkComTest.entity.ServiceRateStatisticsOutput;
import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class ServiceRateWindowProcess extends KeyedProcessFunction<Tuple, ConversationInfo, ServiceRateStatisticsOutput> {

    /**
     * 对话次数
     *
     */
    private ValueState<Long> cntStatus;

    /**
     * 无法回答对话记录
     *
     */
    private OutputTag<ConversationInfo> noAnswerConversation;

    @Override
    public void open(Configuration parameters) throws Exception {
        cntStatus = getRuntimeContext().getState(new ValueStateDescriptor<Long>("cntStatus", Long.class));
        noAnswerConversation = new OutputTag<ConversationInfo>("noAnswerConversation"){};
    }

    @Override
    public void processElement(ConversationInfo value, Context ctx, Collector<ServiceRateStatisticsOutput> out) throws Exception {
        // 日期
        String day = ctx.getCurrentKey().getField(0);
        // 主账号
        String sysCompanyId = ctx.getCurrentKey().getField(1);
        // 子账号
        String sysUserId = ctx.getCurrentKey().getField(2);
        // 机器人标识
        String robotId = ctx.getCurrentKey().getField(3);
        // 回复类型
        String reply = ctx.getCurrentKey().getField(4);
        // 会输名称
        String wordsName = value.getWordsName();
        if(Constants.ReplyType.NOANSWER.getValue().equals(reply)){
            ctx.output(noAnswerConversation, value);
        }
        Long cnt = null == cntStatus.value() ? 0L : cntStatus.value();
        cntStatus.update(cnt + 1);
        ServiceRateStatisticsOutput output = new ServiceRateStatisticsOutput(robotId, wordsName, reply, cntStatus.value(), day, sysUserId, sysCompanyId);
        out.collect(output);
    }
}
