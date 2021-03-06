package com.cribl.ydorego.logcollection.model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Encapsulate customer log request fields.
 * 
 */
public class LogCollectionRequest {

    /**
     * Filename include extension.
     */
    @NotNull
    private final String fileName;

    /**
     * Number of events to return
     */
    @NotNull
    @Min(1)
    @Max(250)
    private final Integer numberOfEvents;

    /**
     * Line filter to match
     */
    private final String filter;

    /**
     * Time when the request was received
     */
    private Date timeRequested;

    /**
     * List of server to fetch from...
     */
    private final String serverList;

    public LogCollectionRequest(String fileName, Integer numberOfEvents, String filter) {
        this.fileName = fileName;
        this.numberOfEvents = numberOfEvents;
        this.filter = filter;
        this.timeRequested = new Date();
        this.serverList = null;
    }

    public LogCollectionRequest(String fileName, Integer numberOfEvents, String filter, String serverList) {
        this.fileName = fileName;
        this.numberOfEvents = numberOfEvents;
        this.filter = filter;
        this.timeRequested = new Date();
        this.serverList = serverList;
    }

    public String getFileName() {
        return fileName;
    }

    
    public Integer getNumberOfEvents() {
        return numberOfEvents;
    }

    public String getFilter() {
        return filter;
    }
 
    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

   
    public Date getTimeRequested() {
        return timeRequested;
    }

    
    public String getServerList() {
        return serverList;
    }

    @Override
    public String toString() {
        if (serverList != null) {
            return "LogCollectionRequest [fileName=" + fileName + ", filter=" + filter + ", numberOfEvents="
                + numberOfEvents + ", serverList=" + serverList + ", timeRequested=" + timeRequested + "]";
        } else {
            return "LogCollectionRequest [fileName=" + fileName + ", filter=" + filter + ", numberOfEvents="
            + numberOfEvents + ", timeRequested=" + timeRequested + "]";

        }
    }
   
    
}
