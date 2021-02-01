package lijie.test.nettyServer.handller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 大文本或大二进制数据处理器
 */
public class ContinuationWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof ContinuationWebSocketFrame) {

        } else {
            ctx.fireChannelRead(msg.retain());
            System.out.println(msg.retain()+"2");
        }
    }
}
