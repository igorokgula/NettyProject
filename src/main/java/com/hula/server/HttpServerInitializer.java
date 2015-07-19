package com.hula.server;

/**
 * Created by Igor on 18.07.2015.
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.router.Handler;
import io.netty.handler.codec.http.router.Router;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Router router = new Router()
            .GET("/hello", HttpHelloWorldServerHandler.class)
            .GET("/redirect", HttpRedirectServerHandler.class)
            .GET("/status", HttpStatusServerHandler.class);

    private static final Handler handler = new Handler(router);

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        //p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));

        // Encoder
        //p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));

        p.addLast(new HttpRequestDecoder());
//         Uncomment the following line if you don't want to handle HttpChunks.
        //p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        //p.addLast(new HttpContentCompressor());

        p.addLast("generalHandler", new HttpGeneralHandler());

        p.addLast(handler.name(), handler);
    }
}
