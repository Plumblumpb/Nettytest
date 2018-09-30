package A3NettyDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
	
	public static void main(String[] args) {
//		负责接收客户端的连接并将SocketChannel交给WorkerEventLoopGroup来进行IO处理。
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
//		负责io处理
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
//			配置线程模型
			serverBootstrap.group(parentGroup, childGroup);
//			配置channel类型。
			serverBootstrap.channel(NioServerSocketChannel.class);
//			配置socket的标准参数
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
			                         .option(ChannelOption.SO_KEEPALIVE, true)
//			配置ChannelHandler（channel处理方式），ChannelPipeline维护着一个ChannelHandler的链表队列
			                         .childHandler(new ChannelInitializer<SocketChannel>() {
										@Override
										protected void initChannel(SocketChannel ch)
												throws Exception {
//											配置解码器
											ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,Delimiters.lineDelimiter()[0]));
//											自定义处理器
											ch.pipeline().addLast(new SimpleHandler());
//											配置编码器
											ch.pipeline().addLast(new StringEncoder());
										}
									});
//			当令Channel开始一个I/O操作时,会创建一个新的ChannelFuture去异步完成操作.
			ChannelFuture future = serverBootstrap.bind(8080).sync();
			future.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
		
		
		
		
	}

}
