package lijie.test.flinkComTest.streaming.process;


import lijie.test.flinkComTest.entity.VisitDataStatisticsOutput;
import lijie.test.flinkComTest.entity.kafka.ConversationInfo;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.calcite.shaded.com.google.common.collect.Lists;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;

import java.util.Optional;

public class VisitDataCountWindowProcess extends KeyedProcessFunction<Tuple, ConversationInfo, VisitDataStatisticsOutput> {

    /**
     * 互动次数
     *
     */
    private ValueState<Long> actionCntStatus;

    /**
     * 会话id和最近时间
     *
     */
    private MapState<String, Long> lastTimeStatus;

    /**
     * 会话总时长
     *
     */
    private ValueState<Long> durationStatus;

    /**
     * 使用机器人（话术）数量
     *
     */
    private ListState<String> robotCntStatus;

    @Override
    public void open(Configuration parameters) throws Exception {
        actionCntStatus = getRuntimeContext().getState(new ValueStateDescriptor<Long>("actionCntStatus", Long.class));
        lastTimeStatus = getRuntimeContext().getMapState(new MapStateDescriptor<String, Long>("lastTimeStatus", String.class, Long.class));
        durationStatus = getRuntimeContext().getState(new ValueStateDescriptor<Long>("durationStatus", Long.class));
        robotCntStatus = getRuntimeContext().getListState(new ListStateDescriptor<String>("robotCntStatus", String.class));
    }

    @Override
    public void processElement(ConversationInfo value, Context ctx, Collector<VisitDataStatisticsOutput> out) throws Exception {
        // 日期
        String day = ctx.getCurrentKey().getField(0);
        // 主账号
        String sysCompanyId = ctx.getCurrentKey().getField(1);
        // 子账号
        String sysUserId = ctx.getCurrentKey().getField(2);

        Long preActionCnt = null == actionCntStatus.value() ? 0L : actionCntStatus.value();
        Long preDuration = null == durationStatus.value() ? 0L : durationStatus.value();
        // 互动次数增量
        int actionAcc = 0;
        // 访问时长增量
        Long durationAcc = 0L;

        Optional<Long> lastTime = Optional.ofNullable(lastTimeStatus.get(value.getSessionId()));
        if(lastTime.isPresent()){
            if(lastTime.get().compareTo(value.getTime().getTime()) < 0){
                durationAcc += value.getTime().getTime() - lastTime.get();
                lastTimeStatus.put(value.getSessionId(), value.getTime().getTime());
            }
        } else {
            lastTimeStatus.put(value.getSessionId(), value.getTime().getTime());
        }

        if(!StringUtils.isNullOrWhitespaceOnly(value.getUserInput()) && !StringUtils.isNullOrWhitespaceOnly(value.getRobotOutput())){
            actionAcc++;
        }

        if(!Lists.newArrayList(robotCntStatus.get()).contains(value.getRobotId())){
            robotCntStatus.add(value.getRobotId());
        }
        // 更新互动次数和通话时长状态
        actionCntStatus.update(preActionCnt + actionAcc);
        durationStatus.update(preDuration + durationAcc);

        System.out.println("lastTimeStatus: " + Lists.newArrayList(lastTimeStatus.keys().iterator()));
        // 生成结果
        VisitDataStatisticsOutput cntResult = new VisitDataStatisticsOutput();
        cntResult.setDayStr(day);
        cntResult.setSysCompanyId(sysCompanyId);
        cntResult.setSysUserId(sysUserId);
        cntResult.setVisitCnt(Long.valueOf(Lists.newArrayList(lastTimeStatus.keys().iterator()).size()));
        cntResult.setActionCnt(actionCntStatus.value());
        cntResult.setActionAvgCnt(cntResult.getActionCnt()/cntResult.getVisitCnt());
        // 总互动时长
        cntResult.setActionAvgDuration(durationStatus.value()/1000);
        cntResult.setRobotCnt(Long.valueOf(Lists.newArrayList(robotCntStatus.get()).size()));
        out.collect(cntResult);
    }
}
