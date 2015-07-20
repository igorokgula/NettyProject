package com.hula.server;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.*;
import com.hula.domain.FullRequest;
import com.hula.domain.Request;
import com.hula.domain.TotalInformation;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Igor on 18.07.2015.
 */
public class HttpStatusServerHandler extends ChannelInboundHandlerAdapter {

    private final StringBuilder buf = new StringBuilder();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        buf.setLength(0);
        sendStatus(ctx);

        super.channelRead(ctx, msg);
    }

    private void sendStatus(ChannelHandlerContext ctx) {
        TotalInformation inf = TotalInformation.getInstance();

        Document document = new Document(DocumentType.HTMLStrict);
        Body body = document.body;
        body.appendChild(new Title().appendText("STATUS"));
        body.appendChild(new H1().appendText("STATUS"));
        body.appendChild(new P().appendText("TOTAL REQUEST: " + inf.getRequestTotal()));
        body.appendChild(new P().appendText("UNIQUE REQUEST: " + inf.getRequestsSize()));
        body.appendChild(new P().appendText("OPEN CONNECTIONS: " + inf.getChannelsSize()));

        body.appendChild(new H2().appendText("REQUEST COUNTER"));
        Table table = new Table();
        table.appendChild(new Thead().appendChild(new Td().appendText("IP ADDRESS"))
                .appendChild(new Td().appendText("TOTAL REQUESTS"))
                .appendChild(new Td().appendText("LAST REQUEST")));
        Tbody tbody = new Tbody();
        table.appendChild(tbody);
        for (Request r: inf.getRequestsInfo()) {
            tbody.appendChild(appendRow(r.toStrings()));
        }
        body.appendChild(table);

        body.appendChild(new H2().appendText("REDIRECTS"));
        table = new Table();
        table.appendChild(new Thead().appendChild(new Td().appendText("URL"))
                .appendChild(new Td().appendText("REDIRECT COUNT")));
        tbody = new Tbody();
        table.appendChild(tbody);
        for (Map.Entry<String, Long> entry: inf.getRedirects().entrySet()) {
            List<String> strings = new ArrayList<String>();
            strings.add(entry.getKey());
            strings.add(entry.getValue().toString());
            tbody.appendChild(appendRow(strings));
        }
        body.appendChild(table);

        body.appendChild(new H2().appendText("ALL CONNECTIONS"));
        table = new Table();
        table.appendChild(new Thead().appendChild(new Td().appendText("SRC_IP"))
                        .appendChild(new Td().appendText("URI"))
                        .appendChild(new Td().appendText("TIMESTAMP"))
                        .appendChild(new Td().appendText("SENT_BYTES"))
                        .appendChild(new Td().appendText("RECEIVED_BYTES"))
                        .appendChild(new Td().appendText("SPEED"))
        );
        tbody = new Tbody();
        table.appendChild(tbody);
        for (FullRequest r: inf.getFullRequestsInfo(16)) {
            tbody.appendChild(appendRow(r.toStrings()));
        }
        body.appendChild(table);

        buf.setLength(0);
        System.out.println(document.write());
        buf.append(document.write());

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");

        ctx.writeAndFlush(response);
    }

    private Tr appendRow(List<String> strings) {
        Tr tr = new Tr();
        for (String s: strings) {
            tr.appendChild(new Td().appendText(s));
        }
        return tr;
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
