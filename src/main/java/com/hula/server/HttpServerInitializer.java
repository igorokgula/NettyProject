package com.hula.server;

import com.hula.domain.FullRequest;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.sql.Timestamp;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        FullRequest fullRequest = new FullRequest();
        fullRequest.setTime(new Timestamp(System.currentTimeMillis()));

        p.addLast("generalHandler", new HttpChannelTrafficShapingHandler(600000, fullRequest));
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new HttpContentCompressor());
        p.addLast(new HttpServerHandler(fullRequest));
    }
}
