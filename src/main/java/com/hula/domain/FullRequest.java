package com.hula.domain;

import java.sql.Timestamp;

/**
 * Created by Igor on 19.07.2015.
 */
public class FullRequest {
    private String ip;
    private String uri;
    private Timestamp time;
    private Long sentBytes;
    private Long receivedBytes;
    private Long speed;

    public FullRequest() {
    }

    public FullRequest(String ip, String uri, Timestamp time, Long sentBytes, Long receivedBytes, Long speed) {
        this.ip = ip;
        this.uri = uri;
        this.time = time;
        this.sentBytes = sentBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return ip + "      " + uri + "              " + time + "          " + sentBytes +
                "                    " + receivedBytes +"                       " + speed;
    }
}
