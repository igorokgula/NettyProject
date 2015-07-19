package com.hula.server;

import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Routed;
import io.netty.util.AttributeKey;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpHeaders.Names;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpRedirectServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("HttpRedirectServerHandler");
        TotalInformation.getInstance().onRequestRedirect();

        if (msg instanceof Routed) {
            Routed routed = (Routed) msg;
            String newUrl = routed.queryParam("url");

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, MOVED_PERMANENTLY);
            response.headers().set(HttpHeaders.Names.HOST, "google.com");
            response.headers().set(HttpHeaders.Names.LOCATION, newUrl);

//            if (!keepAlive) {
//                channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
//            } else {
//                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//                channelHandlerContext.write(response);
//            }
            System.out.println(newUrl);
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
