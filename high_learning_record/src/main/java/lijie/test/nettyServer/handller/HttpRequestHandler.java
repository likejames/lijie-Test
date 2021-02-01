package lijie.test.nettyServer.handller;


import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 处理http请求
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private static final String WS = "ws";

    private static final String HTTP = "http";


    /**
     * 监听客户端注册
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 新客户端连接，通知其他客户端
        System.out.println("新客户端连接");
        String msg = "用户上线";
        ctx.writeAndFlush(msgPot(msg));
    }

    /**
     * 监听客户端断开
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("监听客户端断开");
    }


    /**
     * 监听连接异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close(); // 关闭
    }

    /**
     * 封装消息
     *
     * @param msg
     * @return
     */
    public TextWebSocketFrame msgPot(String msg) {
        return new TextWebSocketFrame(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        System.out.println("ssssssssssssssssssssssssssss");
        ChannelPipeline channelPipeline = channelHandlerContext.pipeline();
        String uri = fullHttpRequest.uri();
        String protocol = getHttpOrWs(uri);
        System.out.println("uri: " + protocol + "\n");
        System.out.println("body: " + fullHttpRequest.retain() + "\n");
        channelPipeline.addLast("websocket协议处理器", new WebSocketServerProtocolHandler("/ws"));
//        channelPipeline.addLast(new MyServerHandler()); // 自定义处理器
        channelPipeline.addLast("websocket数据帧处理器", new WebSocketHandler());
        channelPipeline.addLast("超大文本或者二进制数据数据帧处理器", new ContinuationWebSocketFrameHandler());
        channelPipeline.addLast("TextWebsocket数据帧处理", new TextWebSocketFrameHandler());
        channelHandlerContext.fireChannelRead(fullHttpRequest.retain());
    }
    /**
     * 分割子url
     *
     * @param uri
     * @return
     */
    private String getHttpOrWs(String uri) {
        String[] contents = uri.split("/");
        int length = contents.length;
        if (length < 2) {
            return "";
        } else {
            return contents[1];
        }
    }


}
