package A2Netty实现http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


/**
 * @Auther: cpb
 * @Date: 2018/9/26 16:02
 * @Description:
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpPipelineInitializer(boolean client){
        this.client = client;
    }
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        if(client){
//            用于处理来自 server 响应
            pipeline.addLast("decoder",new HttpResponseDecoder());
//            用于发送请求到 server
            pipeline.addLast("encoder", new HttpRequestEncoder());

        }else{
//            用于接收来自 client 的请求
            pipeline.addLast("decoder", new HttpRequestDecoder());  //3
//            用来发送响应给 client
            pipeline.addLast("encoder", new HttpResponseEncoder());  //4
        }
    }
}
