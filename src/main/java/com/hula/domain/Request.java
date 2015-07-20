package com.hula.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Igor on 18.07.2015.
 */
public class Request {

    private String ip;
    private Integer requestCount = 0;
    private Timestamp dateOfLastRequest;

    public Request() {
    }

    public Request(String ip, Integer requestCount, Timestamp dateOfLastRequest) {
        this.ip = ip;
        this.requestCount = requestCount;
        this.dateOfLastRequest = dateOfLastRequest;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Timestamp getDateOfLastRequest() {
        return dateOfLastRequest;
    }

    public void setDateOfLastRequest(Timestamp dateOfLastRequest) {
        this.dateOfLastRequest = dateOfLastRequest;
    }

    @Override
    public String toString() {
        return String.format("%-15s%-19s%-25s", ip, requestCount, dateOfLastRequest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (ip != null ? !ip.equals(request.ip) : request.ip != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        return result;
    }
}
