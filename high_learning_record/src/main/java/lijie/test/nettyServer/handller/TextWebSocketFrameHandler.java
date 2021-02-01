package lijie.test.nettyServer.handller;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 判断websocket消息类型
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("TextWebSocketFrameHandler异常！！！！！", cause);
    }

    /**
     * 对文本消息进行类型判断，添加对应的处理器
     *
     * @param ctx
     * @param webSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) webSocketFrame;
            try {
                System.out.println(textWebSocketFrame.text());
            } catch (Exception e) {
                logger.error("websocket业务处理异常！", e);
            }
        }
    }

    private void doService(TextWebSocketFrame textWebSocketFrame, ChannelHandlerContext ctx) throws Exception {

    }

}
