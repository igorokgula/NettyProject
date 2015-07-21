package com.hula.domain;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Igor on 18.07.2015.
 */
public class TotalInformation {

    private static final TotalInformation instance = new TotalInformation();

    private static final Integer MAX_FULL_REQUESTS_COUNT = 1000;

    private Integer requestTotal = 0;


    private List<Request> requests = new CopyOnWriteArrayList<Request>();

    private List<FullRequest> fullRequests = new CopyOnWriteArrayList<FullRequest>();

    private Map<String, Long> redirects = new ConcurrentHashMap<String, Long>();

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private TotalInformation(){}

    public static TotalInformation getInstance() {
        return instance;
    }

    public synchronized void onRequest(String ipAddress, Timestamp time) {        
        requestTotal++;

        Request request = new Request(ipAddress, 1, time);
        if (!requests.contains(request)) {
            requests.add(request);
        } else {
            Request r = requests.get(requests.indexOf(request));
            r.setRequestCount(r.getRequestCount() + 1);
            r.setDateOfLastRequest(time);
        }
    }

    public synchronized void onFullRequest(FullRequest fullRequest) {
        fullRequests.add(fullRequest);
        if (fullRequests.size() > MAX_FULL_REQUESTS_COUNT) {
            fullRequests.remove(0);
        }
    }

    public void addChannel(Channel channel) {
        synchronized (this) {
            channels.add(channel);
        }
    }

    public synchronized void onRedirect(String url) {
        if (redirects.containsKey(url)) {
            redirects.put(url, redirects.get(url) + 1);
        } else {
            redirects.put(url, 1L);
        }
    }

    public List<FullRequest> getFullRequestsInfo(Integer countOfRecords){
        List<FullRequest> rez = new ArrayList<FullRequest>();
        Integer size = (countOfRecords > fullRequests.size()) ? fullRequests.size() : countOfRecords;
        for (int i = 0; i < size; i++) {
            try {
                rez.add(fullRequests.get(fullRequests.size() - size + i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return rez;
    }

    public List<Request> getRequestsInfo() {
        List<Request> rez = new ArrayList<Request>();
        for (Request r: requests) {
            try {
                rez.add(r.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return rez;
    }

    public Map<String, Long> getRedirects() {
        Map<String, Long> rez = new HashMap<String, Long>();
        for (Map.Entry<String, Long> entry: redirects.entrySet()) {
            rez.put(entry.getKey(), entry.getValue());
        }
        return rez;
    }

    public Integer getRequestsSize() {
        return requests.size();
    }

    public Integer getRequestTotal() {
        return requestTotal;
    }

    public Integer getChannelsSize() {
        return channels.size();
    }

}
