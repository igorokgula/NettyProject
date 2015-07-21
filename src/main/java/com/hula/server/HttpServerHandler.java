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
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.Object;
import java.util.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Igor on 21.07.2015.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;

    private FullRequest fullRequest;

    private StringBuilder buf = new StringBuilder();

    private static final String HELLO_URL = "hello";
    private static final String REDIRECT_URL = "redirect";
    private static final String STATUS_URL = "status";

    public HttpServerHandler(FullRequest fullRequest) {
        this.fullRequest = fullRequest;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (o instanceof HttpRequest) {
            request = (HttpRequest) o;
            invokeRightMethod(channelHandlerContext);
            fullRequest.setUri(request.getUri());
        }
    }

    private void invokeRightMethod(ChannelHandlerContext channelHandlerContext) {
        HttpMethod method = request.getMethod();

        if (method != HttpMethod.GET) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.NOT_IMPLEMENTED);
            channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

        List<String> urlParts = Arrays.asList(request.getUri().replaceFirst("^/", "").split("/"));

        if (urlParts.size() == 1) {
            Map<String, List<String>>  params = new QueryStringDecoder(request.getUri()).parameters();
            if (urlParts.get(0).equals(HELLO_URL) && (params.size() == 0)) {
                sendHello(channelHandlerContext);
            } else if (urlParts.get(0).equals(STATUS_URL) && (params.size() == 0)) {
                sendStatus(channelHandlerContext);
            } else if (urlParts.get(0).substring(0,8).equals(REDIRECT_URL) && (params.size() == 1)) {
                sendRedirect(params.get("url").get(0), channelHandlerContext);
            } else {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.NOT_FOUND);
                channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void sendRedirect(String newUrl, ChannelHandlerContext channelHandlerContext) {
        TotalInformation.getInstance().onRedirect(newUrl);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, "http://" + newUrl);

        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendHello(final ChannelHandlerContext channelHandlerContext) {
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
        for (java.util.Map.Entry<String, Long> entry: inf.getRedirects().entrySet()) {
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

}
