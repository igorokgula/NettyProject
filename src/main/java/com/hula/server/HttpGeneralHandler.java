package com.hula.server;

import com.hula.domain.FullRequest;
import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpGeneralHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("HttpGeneralHandler");
        String ip = getIP(channelHandlerContext);
        TotalInformation.getInstance().onRequest(ip, new Date());
        TotalInformation.getInstance().onFullRequest(ip, new FullRequest("", new Timestamp(System.currentTimeMillis()),
                0, 0, 0));
        super.channelRead(channelHandlerContext, o);
    }

    private String getIP(ChannelHandlerContext channelHandlerContext) {
        InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        InetAddress inetaddress = socketAddress.getAddress();
        return inetaddress.getHostAddress();
    }

}
