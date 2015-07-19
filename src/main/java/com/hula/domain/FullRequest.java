package com.hula.domain;

import java.sql.Timestamp;

/**
 * Created by Igor on 19.07.2015.
 */
public class FullRequest {
    private String uri;
    private Timestamp time;
    private Integer sentBytes;
    private Integer receivedBytes;
    private Integer speed;

    public FullRequest() {
    }

    public FullRequest(String uri, Timestamp time, Integer sentBytes, Integer receivedBytes, Integer speed) {
        this.uri = uri;
        this.time = time;
        this.sentBytes = sentBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Integer sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Integer getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Integer receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
}
