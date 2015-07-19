package com.hula.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 18.07.2015.
 */
public class TotalInformation {

    private static final TotalInformation instance = new TotalInformation();

    private Integer requestTotal = 0;

    private Integer requestTotalHello = 0;
    private Integer requestTotalRedirect = 0;
    private Integer requestTotalStatus = 0;

    private Map<String, Request> requestMap = new HashMap<String, Request>();

    private Map<String, FullRequest> fullRequestMap = new HashMap<String, FullRequest>();

    private TotalInformation(){}

    public static TotalInformation getInstance() {
        return instance;
    }

    public synchronized void onRequest(String ipAddress, Date date) {
        requestTotal++;
        Request request = requestMap.get(ipAddress);
        if (request == null) {
            Request newRequest = new Request();
            newRequest.setRequestCount(1);
            newRequest.setDateOfLastRequest(date);
            requestMap.put(ipAddress, newRequest);
        } else {
            Integer oldRequestCount = request.getRequestCount();
            request.setRequestCount(oldRequestCount + 1);
            request.setDateOfLastRequest(date);
        }
    }

    public synchronized void onFullRequest(String ip, FullRequest fullRequest) {
        fullRequestMap.put(ip, fullRequest);
    }

    public synchronized void onRequestHello() {
        requestTotalHello++;
    }

    public synchronized void onRequestRedirect() {
        requestTotalRedirect++;
    }

    public synchronized void onRequestStatus() {
        requestTotalStatus++;
    }

    public synchronized Map<String, Request> getRequestMap() {
        return requestMap;
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

    public Map<String, FullRequest> getFullRequestMap() {
        return fullRequestMap;
    }
}
