package com.hula.server;

import com.hula.domain.TotalInformation;
import io.netty.channel.*;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpHelloWorldServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(final ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("HttpHelloWorldServerHandler");

        ChannelFuture channelFuture = channelHandlerContext.newSucceededFuture();
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Thread.sleep(10000);
                channelHandlerContext.writeAndFlush("Hello World!!!");
            }
        });
        super.channelRead(channelHandlerContext, o);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
