package A5Handler配置说明;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Auther: cpb
 * @Date: 2018/11/2 11:23
 * @Description:
 */
public class HelloWorldClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));


    public static void main(String[] args) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
//                    配置通道
                    .channel(NioSocketChannel.class)
//                    配置tcp
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
//                            配置请求处理channelpipline（channelHandler链）
                            ChannelPipeline p = ch.pipeline();
//                            解码器
                            p.addLast("decoder", new StringDecoder());
//                            编码器
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new HelloWorldClientHandler());
                        }
                    });
//                创建线程
            ChannelFuture future = b.connect(HOST, PORT).sync();
//            获取通道并写数据
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client");
//              关闭线程
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
