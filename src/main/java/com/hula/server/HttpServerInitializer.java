package com.hula.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.router.Handler;
import io.netty.handler.codec.http.router.Router;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Router router = new Router()
            .GET("/hello", HttpHelloWorldServerHandler.class)
            .GET("/redirect", HttpRedirectServerHandler.class)
            .GET("/status", HttpStatusServerHandler.class);

    private static final Handler handler = new Handler(router);

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpRequestDecoder());

        p.addLast(new HttpResponseEncoder());

        p.addLast(handler.name(), handler);
        p.addLast("generalHandler", new HttpChannelTrafficShapingHandler(1000));
    }
}
