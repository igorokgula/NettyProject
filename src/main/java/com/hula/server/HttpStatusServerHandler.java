package com.hula.server;

import com.hula.domain.Request;
import com.hula.domain.TotalInformation;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpStatusServerHandler extends SimpleChannelInboundHandler<Object> {

    private Map<String, Request> requestMap = new HashMap<String, Request>();

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private HttpRequest request;

    private final StringBuilder buf = new StringBuilder();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        TotalInformation.getInstance().onRequestStatus();

        buf.setLength(0);
        buf.append("STATUS\r\n");
        buf.append("===================================\r\n");

        buf.append("TOTAL REQUESTS: " + TotalInformation.getInstance().getRequestTotal() + "\r\n");

        buf.append("UNIQUE TOTAL REQUESTS: " + TotalInformation.getInstance().getRequestMap().size() + "\r\n\n");

        buf.append("REQUEST COUNTER:\r\n");
        buf.append("IP ADDRESS     TOTAL REQUESTS                   LAST REQUEST\r\n");

        TotalInformation totalInf = TotalInformation.getInstance();
        for (Map.Entry<String, Request> requestEntry : totalInf.getRequestMap().entrySet()) {
            buf.append(requestEntry.getKey() + "           " + requestEntry.getValue().getRequestCount() +
                    "                " + requestEntry.getValue().getDateOfLastRequest() + "\r\n");
        }

        buf.append("\r\n");
        buf.append("URL ADDRESSES: \r\n");
        buf.append("HELLO           REDIRECT             STATUS \r\n");
        buf.append(totalInf.getRequestTotalHello() + "                  " +
                totalInf.getRequestTotalRedirect() + "                  " +
                totalInf.getRequestTotalStatus() + "\r\n");

        buf.append("ALL CONNECTIONS: " + channels.size() + "\r\n");



        System.out.println(buf.toString());

        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection active");
        channels.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
