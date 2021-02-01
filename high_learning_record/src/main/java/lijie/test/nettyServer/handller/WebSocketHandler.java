package lijie.test.nettyServer.handller;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础的WebSocketFrame数据帧
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        channelHandlerContext.fireChannelRead(webSocketFrame.retain());
        TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) webSocketFrame;
        System.out.println(textWebSocketFrame.text() + "1");
    }


}
