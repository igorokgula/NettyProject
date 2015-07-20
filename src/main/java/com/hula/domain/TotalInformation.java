package com.hula.domain;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.*;

/**
 * Created by Igor on 18.07.2015.
 */
public class TotalInformation {

    private static final String URL_STATUS = "/status";
    private static final String URL_REDIRECT = "/redirect";
    private static final String URL_HELLO = "/hello";

    private static final TotalInformation instance = new TotalInformation();

    private Integer requestTotal = 0;

    private Integer requestTotalHello = 0;
    private Integer requestTotalRedirect = 0;
    private Integer requestTotalStatus = 0;

    private List<Request> requests = new ArrayList<Request>();

    private List<FullRequest> fullRequestMap = new ArrayList<FullRequest>();

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private TotalInformation(){}

    public static TotalInformation getInstance() {
        return instance;
    }

    public synchronized void onRequest(String ipAddress, Date date, String url) {
        requestTotal++;

        if (url.equals(URL_HELLO)) {
            requestTotalHello++;
        } else if (url.equals(URL_REDIRECT)) {
            requestTotalRedirect++;
        } else if (url.equals(URL_STATUS)) {
            requestTotalStatus++;
        }

        Request request = new Request(ipAddress, 1, date);
        if (!requests.contains(request)) {
            requests.add(request);
        } else {
            Request r = requests.get(requests.indexOf(request));
            r.setRequestCount(r.getRequestCount() + 1);
            r.setDateOfLastRequest(date);
        }
    }

    public synchronized void onFullRequest(FullRequest fullRequest) {
        fullRequestMap.add(fullRequest);
    }

    public String getFullRequestsString(Integer countOfRecords) {
        StringBuffer stringBuffer = new StringBuffer();
        Integer size = (countOfRecords > fullRequestMap.size()) ? fullRequestMap.size() : countOfRecords;
        for (int i = 0; i < size; i++) {
            stringBuffer.append(fullRequestMap.get(fullRequestMap.size() - size + i).toString() + "\r\n");
        }
        return stringBuffer.toString();
    }

    public synchronized void addChannel(Channel channel) {
        channels.add(channel);
    }

    public String getRequestsString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Request r: requests) {
            stringBuffer.append(r.toString() + "\r\n");
        }
        return stringBuffer.toString();
    }

    public Integer getRequestsSize() {
        return requests.size();
    }

    public Integer getRequestTotal() {
        return requestTotal;
    }

    public Integer getRequestTotalHello() {
        return requestTotalHello;
    }

    public Integer getRequestTotalRedirect() {
        return requestTotalRedirect;
    }

    public Integer getRequestTotalStatus() {
        return requestTotalStatus;
    }

    public Integer getChannelsSize() {
        return channels.size();
    }
}
