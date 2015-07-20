package com.hula.server;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.H1;
import com.hp.gagawa.java.elements.Title;
import com.hula.domain.TotalInformation;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpHelloWorldServerHandler extends ChannelInboundHandlerAdapter {

    private StringBuilder buf = new StringBuilder();

    @Override
    public void channelRead(final ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("HttpHelloWorldServerHandler");
        Document document = new Document(DocumentType.HTMLTransitional);
        document.body.appendChild(new Title().appendText("Hello!"))
                .appendChild(new H1().appendText("Hello World!!!"));

        buf.setLength(0);
        buf.append(document.write());
        channelHandlerContext.executor().schedule(new Runnable() {
            public void run() {
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");

                channelHandlerContext.writeAndFlush(response);
            }
        }, 10, TimeUnit.SECONDS);
        super.channelRead(channelHandlerContext, o);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
