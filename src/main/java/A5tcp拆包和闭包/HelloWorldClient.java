package A5tcp拆包和闭包;

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
//                            p.addLast(new BaseClientHandler());
                        }
                    });
//                创建线程
            ChannelFuture future = b.connect(HOST, PORT).sync();
            String req = "In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
                    + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
                    + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
                    + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
                    + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
                    + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
                    + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
                    + "models in general and Netty’s threading model in particular, whose performance and consistency advantag" + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
                    + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
                    + "sdsa ddasd asdsadas dsadasdas";
//            获取通道并写数据
            future.channel().writeAndFlush(req);
//              关闭线程
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
