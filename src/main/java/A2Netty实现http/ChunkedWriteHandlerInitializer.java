package A2Netty实现http;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Auther: cpb
 * @Date: 2018/9/26 16:42
 * @Description:
 */
//编写大型数据
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;
    private final SslContext context;

    public ChunkedWriteHandlerInitializer(File file,SslContext context){
        this.file = file;
        this.context = context;
    }

    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new SslHandler(context.newEngine(channel.alloc()))); //1
        pipeline.addLast(new ChunkedWriteHandler());//2
        pipeline.addLast(new WriteStreamHandler());//3
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {  //4

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
