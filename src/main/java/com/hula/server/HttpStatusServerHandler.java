package com.hula.server;

import com.hula.domain.Request;
import com.hula.domain.TotalInformation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.router.Routed;

import java.util.Map;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpStatusServerHandler extends ChannelInboundHandlerAdapter {

    private final StringBuilder buf = new StringBuilder();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        buf.append("STATUS\r\n");
        buf.append("===================================\r\n");

        buf.append("TOTAL REQUESTS: " + TotalInformation.getInstance().getRequestTotal() + "\r\n");

        buf.append("UNIQUE TOTAL REQUESTS: " + TotalInformation.getInstance().getRequestsSize() + "\r\n\n");

        buf.append("REQUEST COUNTER\r\n");
        buf.append("IP ADDRESS     TOTAL REQUESTS                   LAST REQUEST\r\n");

        TotalInformation totalInf = TotalInformation.getInstance();
        buf.append(TotalInformation.getInstance().getRequestsString());

        buf.append("\r\n");
        buf.append("URL ADDRESSES: \r\n");
        buf.append("HELLO           REDIRECT             STATUS \r\n");
        buf.append(totalInf.getRequestTotalHello() + "                  " +
                totalInf.getRequestTotalRedirect() + "                  " +
                totalInf.getRequestTotalStatus() + "\r\n");

        buf.append("ALL CONNECTIONS: " + TotalInformation.getInstance().getChannelsSize() + "\r\n");

        buf.append("LAST 16\r\n");
        buf.append("SRC_IP         URI                     TIMESTAMP                     SENT_BYTES        " +
                "RECEIVED_BYTES               SPEED\r\n");
        if (msg instanceof LastHttpContent) {
            System.out.println();
        }
        buf.append(TotalInformation.getInstance().getFullRequestsString(16));

        System.out.println(buf);
//        System.out.println(TotalInformation.getInstance().getChannelsSize());
//        System.out.println(TotalInformation.getInstance().getRequestTotal());

        ctx.writeAndFlush(buf);

        buf.setLength(0);

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
