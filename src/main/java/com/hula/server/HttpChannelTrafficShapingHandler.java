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

    private FullRequest fullRequest;

    private boolean flag = true;

    public HttpChannelTrafficShapingHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("HttpChannelTrafficShapingHandler");

        System.out.println(msg.getClass());
        if (msg instanceof Routed) {
            System.out.println("1");
            String ip = getIP(ctx);

            String path = ((Routed)msg).path();

            TotalInformation.getInstance().onRequest(ip, new Timestamp(System.currentTimeMillis()), path);
            fullRequest = new FullRequest(ip, path,
                    new Timestamp(System.currentTimeMillis()), 0L, 0L, 0L);
            TotalInformation.getInstance().onFullRequest(fullRequest);
        }
        super.channelRead(ctx, msg);
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

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println("START");
        this.trafficCounter().start();
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        this.trafficCounter().stop();
        System.out.println("STOP");
        System.out.println(fullRequest);
        System.out.println(trafficCounter());
        Long speed = (this.trafficCounter().cumulativeReadBytes() + this.trafficCounter().cumulativeReadBytes())
                /(System.currentTimeMillis() - fullRequest.getTime().getTime());
        TotalInformation.getInstance().setConnectionInfo(fullRequest, this.trafficCounter().cumulativeReadBytes(),
                this.trafficCounter().cumulativeWrittenBytes(), speed);
    }
}
