package lijie.test.nettyServer.handller;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lijie.test.nettyServer.utils.JsonUtils;

public class MyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 监听客户端注册
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 新客户端连接，通知其他客户端
        for (Channel channel : channels) {
            String msg = "用户上线";
            channel.writeAndFlush(msgPot(msg));
        }
        // 加入队列
        channels.add(ctx.channel());
        //ctx.close(); // 关闭
    }

    /**
     * 监听客户端断开
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 离开时，通知其他客户端
        for (Channel channel : channels) {
            if (channel != ctx.channel()) {
                String msg = "用户离开";
                channel.writeAndFlush(msgPot(msg));
            }
        }
        // 整理队列
        channels.remove(ctx.channel());
    }

    /**
     * 读取客户端发过来的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame tmsg)
            throws Exception {
        // 处理消息
        for (Channel channel : channels) {
            // 判断是否是当前用户的消息
            if (channel != ctx.channel()) {
                Boolean bool = JsonUtils.isJSONValid(tmsg.text());
                if (bool) {
                    String msg = tmsg.text();
                    channel.writeAndFlush(msgPot(msg));
                    System.out.println("-----------是否是报警信息：　"+bool);
                } else {
                    String msg = "用户说："
                            + tmsg.text();
                    channel.writeAndFlush(msgPot(msg));
                }
            } else {
                String msg = "我说：" + tmsg.text();
                channel.writeAndFlush(msgPot(msg));
            }
        }
//        ctx.close();
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

}