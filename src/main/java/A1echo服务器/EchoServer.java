package A1echo服务器;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Auther: cpb
 * @Date: 2018/9/21 15:51
 * @Description:
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new EchoServerHandler());
                        }

                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println(EchoServer.class.getName()+" started "+channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new EchoServer(port).run();
    }


}

