package com.hula.server;

import com.hula.domain.FullRequest;
import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Timestamp;

/**
 * Created by Igor on 20.07.2015.
 */
public class HttpChannelTrafficShapingHandler extends ChannelTrafficShapingHandler {
    private FullRequest fullRequest;

    public HttpChannelTrafficShapingHandler(long checkInterval, FullRequest fullRequest) {
        super(checkInterval);
        this.fullRequest = fullRequest;
    }

    private String getIP(ChannelHandlerContext channelHandlerContext) {
        InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        InetAddress inetaddress = socketAddress.getAddress();
        return inetaddress.getHostAddress();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        TotalInformation.getInstance().onRequest(getIP(ctx), new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TotalInformation.getInstance().addChannel(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.trafficCounter().start();
        fullRequest.setIp(getIP(ctx));
        TotalInformation.getInstance().onFullRequest(fullRequest);
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        this.trafficCounter().stop();
        Long speed = (1000*(this.trafficCounter().cumulativeReadBytes() + this.trafficCounter().cumulativeReadBytes()))
                /(System.currentTimeMillis() - fullRequest.getTime().getTime());
        fullRequest.setSendBytes(this.trafficCounter().cumulativeWrittenBytes());
        fullRequest.setReceivedBytes(this.trafficCounter().cumulativeReadBytes());
        fullRequest.setSpeed(speed);
    }
}
