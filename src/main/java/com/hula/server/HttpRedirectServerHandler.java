package com.hula.server;

import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Routed;

import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpRedirectServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("HttpRedirectServerHandler");

        if (msg instanceof Routed) {
            Routed routed = (Routed) msg;
            String newUrl = routed.queryParam("url");

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
            response.headers().set(HttpHeaders.Names.LOCATION, newUrl);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");

            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            super.channelRead(channelHandlerContext, msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
