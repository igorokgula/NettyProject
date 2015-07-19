package com.hula.server;

import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("HttpHelloWorldServerHandler");
        TotalInformation.getInstance().onRequestHello();

        ChannelFuture channelFuture = channelHandlerContext.newSucceededFuture();
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelFuture.awaitUninterruptibly(10000);
                System.out.println("Hello World!!!");
                channelHandlerContext.writeAndFlush("Hello World!!!");
            }
        });
    }

}
