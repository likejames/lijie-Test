package lijie.test.enumTest;


import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Callable;

public interface WechatMsgTypeJudge {
    String judge(String name);
}
