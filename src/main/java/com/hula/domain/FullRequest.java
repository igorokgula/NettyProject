package com.hula.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 19.07.2015.
 */
public class FullRequest implements Cloneable {
    private String ip;
    private String url;
    private Timestamp time;
    private Long sendBytes;
    private Long receivedBytes;
    private Long speed;

    public FullRequest() {
    }

    public FullRequest(String ip, String url, Timestamp time, Long sendBytes, Long receivedBytes, Long speed) {
        this.ip = ip;
        this.url = url;
        this.time = time;
        this.sendBytes = sendBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Long getSendBytes() {
        return sendBytes;
    }

    public void setSendBytes(Long sendBytes) {
        this.sendBytes = sendBytes;
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
        return String.format("%-15s%-15s%-35s%-15s%-20s%-15s", ip, url, time, sendBytes, receivedBytes, speed);
    }

    @Override
    public FullRequest clone() throws CloneNotSupportedException {
        FullRequest clone = new FullRequest(ip, url, new Timestamp(time.getTime()), sendBytes, receivedBytes, speed);
        return clone;
    }

    public List<String> toStrings() {
        List<String> rez = new ArrayList<String>();
        rez.add(this.getIp());
        rez.add(this.getUrl());
        rez.add(this.getTime() == null ? "" : this.getTime().toString());
        rez.add(this.getSendBytes() == null ? "" : this.getSendBytes().toString());
        rez.add(this.getReceivedBytes() == null ? "" : this.getReceivedBytes().toString());
        rez.add(this.getSpeed() == null ? "" : this.getSpeed().toString());

        return rez;
    }
}
