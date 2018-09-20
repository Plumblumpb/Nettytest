package 丢弃服务器;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Auther: cpb
 * @Date: 2018/9/20 17:31
 * @Description:
 */
/**
 * 处理服务端 channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
//    在收到消息时被调用，这个例子中，收到的消息的类型是 ByteBuf
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // 默默地丢弃收到的数据
//        ByteBuf 是一个引用计数对象
        ((ByteBuf) msg).release(); // (3)

//        try {
//            // Do something with msg
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
