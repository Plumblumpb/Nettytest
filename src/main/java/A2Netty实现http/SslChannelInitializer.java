package A2Netty实现http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Auther: cpb
 * @Date: 2018/9/26 15:52
 * @Description:
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
//    ssl是否启用
    private final boolean startTls;
//  是启用client模式还是Server模式
    private final boolean client;

    public SslChannelInitializer(SslContext context, boolean client, boolean startTls){
        this.context = context;
        this.startTls  =  startTls;
        this.client = client;

    }

    protected void initChannel(Channel channel) throws Exception {
//        给每个 SslHandler 实例使用一个新的 SslEngine
        SSLEngine engine = context.newEngine(channel.alloc());
//        设置 SslEngine 是 client 或者是 server 模式
        engine.setUseClientMode(client);
//        添加 SslHandler 到 pipeline 作为第一个处理器
        channel.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
