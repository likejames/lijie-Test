package lijie.test.nettyServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lijie.test.nettyServer.handller.HttpRequestHandler;
import lijie.test.nettyServer.handller.MyServerHandler;
import lijie.test.nettyServer.handller.WebSocketServerHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

class NettyServer {

    private int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() {

        // 创建服务类
        ServerBootstrap sb = new ServerBootstrap();

        // 创建boss和woker
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup woker = new NioEventLoopGroup();

        try {
            // 设置线程池
            sb.group(boss, woker);

            // 设置channel工厂
            sb.channel(NioServerSocketChannel.class);

            // 设置管道
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

//                    ch.pipeline().addLast(new HttpServerCodec());// websocket基于http协议，需要HttpServerCodec
//                    ch.pipeline().addLast(new HttpObjectAggregator(65535)); // http消息组装
//                    ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws")); // websocket通信支持
//                    ch.pipeline().addLast(new MyServerHandler()); // 自定义处理器
//
                    ch.pipeline().addLast("http编解码器", new HttpServerCodec());
                    ch.pipeline().addLast("http大数据包处理器", new ChunkedWriteHandler());
                    ch.pipeline().addLast("http报文聚合器", new HttpObjectAggregator(64 * 1024));
//                    ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws")); // websocket通信支持
//                    ch.pipeline().addLast("自定义处理器", new WebSocketServerHandler());
                    ch.pipeline().addLast("自定义处理器", new HttpRequestHandler());

                }
            });

            // 服务器异步创建绑定
            ChannelFuture cf = sb.bind(port).sync();

            // 等待服务端关闭
            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            woker.shutdownGracefully();
        }
    }

    /**
     * server启动
     *
     * @param args
     */
    public static void main(String[] args) {
        new NettyServer(8088).start();
    }
}