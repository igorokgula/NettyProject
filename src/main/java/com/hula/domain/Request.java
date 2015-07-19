package com.hula.domain;

import java.util.Date;

/**
 * Created by Igor on 18.07.2015.
 */
public class Request {

    private Integer requestCount = 0;
    private Date dateOfLastRequest;

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Date getDateOfLastRequest() {
        return dateOfLastRequest;
    }

    public void setDateOfLastRequest(Date dateOfLastRequest) {
        this.dateOfLastRequest = dateOfLastRequest;
    }
}
