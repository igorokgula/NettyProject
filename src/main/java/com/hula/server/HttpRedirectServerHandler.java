package com.hula.server;

import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Routed;

import java.net.URI;

import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpRedirectServerHandler extends ChannelInboundHandlerAdapter {

    private StringBuilder buf = new StringBuilder();

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("HttpRedirectServerHandler");

        if (msg instanceof Routed) {
            Routed routed = (Routed) msg;
            String newUrl = routed.queryParam("url");
            TotalInformation.getInstance().onRedirect(newUrl);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
            response.headers().set(HttpHeaders.Names.LOCATION, "http://" + newUrl);

            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            super.channelRead(channelHandlerContext, msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
