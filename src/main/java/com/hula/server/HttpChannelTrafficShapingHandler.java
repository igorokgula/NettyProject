package com.hula.server;

import com.hula.domain.FullRequest;
import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.router.Routed;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Igor on 20.07.2015.
 */
public class HttpChannelTrafficShapingHandler extends ChannelTrafficShapingHandler {

    public HttpChannelTrafficShapingHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("HttpChannelTrafficShapingHandler");
        requestInf(ctx, msg);
        super.channelRead(ctx, msg);
    }

    public void requestInf(ChannelHandlerContext channelHandlerContext, Object msg) {
        if (msg instanceof Routed) {
            String ip = getIP(channelHandlerContext);

            Long readBytes = trafficCounter.cumulativeReadBytes();
            Long writtenBytes = trafficCounter.cumulativeWrittenBytes();
            Long speed = trafficCounter.lastReadBytes();

            String path = ((Routed)msg).path();

            TotalInformation.getInstance().onRequest(ip, new Timestamp(System.currentTimeMillis()), path);
            TotalInformation.getInstance().onFullRequest(new FullRequest(ip, path,
                    new Timestamp(System.currentTimeMillis()), writtenBytes, readBytes, speed));
        }
    }

    private String getIP(ChannelHandlerContext channelHandlerContext) {
        InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        InetAddress inetaddress = socketAddress.getAddress();
        return inetaddress.getHostAddress();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TotalInformation.getInstance().addChannel(ctx.channel());
        super.channelActive(ctx);
    }
}
