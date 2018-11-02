package A4NettyHelloWorld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Auther: cpb
 * @Date: 2018/11/2 10:34
 * @Description:
 */
public class HelloWorldServer {
//    端口号
    private int port;
//    构造函数
    public HelloWorldServer(int port){
        this.port = port;
    }

    public void start(){
//        boss线程，接收客户端tcp请求并派发给worker线程（默认数目为1）
        EventLoopGroup boss = new NioEventLoopGroup();
//        work线程，处理io，执行系统任务(默认数目为cpu*2)
        EventLoopGroup  worker = new NioEventLoopGroup();

//        配置基本参数
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
//                          配置线程
            serverBootstrap.group(boss,worker)
                            //配置channel类型。
                           .channel(NioServerSocketChannel.class)
//                          配置tcp的backlog参数
                           .option(ChannelOption.SO_BACKLOG, 128)
                           .childOption(ChannelOption.SO_KEEPALIVE, true)

//                            配置请求处理channelpipline（channelHandler链）
                           .childHandler(new ChannelInitializer<SocketChannel>() {

                               protected void initChannel(SocketChannel ch) throws Exception {
//                                   解码器
                                   ch.pipeline().addLast("decoder", new StringDecoder());
//                                   编码器
                                   ch.pipeline().addLast("encoder", new StringEncoder());
//                                   处理逻辑
                                   ch.pipeline().addLast(new HelloWorldServerHandler());
                               };

                           });

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = serverBootstrap.bind(port).sync();

            System.out.println("Server start listen at " + port );
            future.channel().closeFuture().sync();



        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {

        new HelloWorldServer(8080).start();
    }

}
