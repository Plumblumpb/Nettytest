package A2Netty实现http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Auther: cpb
 * @Date: 2018/9/26 16:12
 * @Description:
 */
//由于 HTTP 请求和响应可以由许多部分组合而成，你需要聚合他们形成完整的消息。为了消除这种繁琐任务， Netty 提供了一个聚合器,合并消息部件到 FullHttpRequest 和 FullHttpResponse 消息。这样您总是能够看到完整的消息内容。
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean client;
//    启用https
    private final SslContext context;

    public HttpAggregatorInitializer(boolean client,SslContext context){
        this.client = client;
        this.context = context;
    }

    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();

//        启用https
        SSLEngine engine = context.newEngine(channel.alloc());
        channelPipeline.addFirst("ssl", new SslHandler(engine));  //1

        if(client){
            channelPipeline.addLast("codec",new HttpClientCodec());
//           解压
            channelPipeline.addLast("decompressor" , new HttpContentDecompressor());
//            启用https
            channelPipeline.addLast("codec", new HttpClientCodec());
        }else {
            channelPipeline.addLast("codec",new HttpServerCodec());
//            压缩
            channelPipeline.addLast("compressor",new HttpContentCompressor());

//            启用https
            channelPipeline.addLast("codec", new HttpServerCodec());
        }
        channelPipeline.addLast("aggregator", new HttpObjectAggregator(512*1024));
    }
}
