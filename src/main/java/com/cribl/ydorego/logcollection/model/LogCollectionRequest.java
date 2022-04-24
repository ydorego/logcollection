package com.cribl.ydorego.logcollection.model;

import java.util.Date;

/**
 * Encapsulate customer log request information.
 * 
 */
public class LogCollectionRequest {

    /**
     * Filename include extension.
     */
    private final String fileName;

    /**
     * Number of events to return
     */
    private final Integer numberOfEvents;

    /**
     * Line fitler to match
     */
    private final String matchingFilter;

    /**
     * Time when the request was received
     */
    private Date timeRequested;
    
    public LogCollectionRequest(String fileName, Integer numberOfEvents, String matchingFilter) {
        this.fileName = fileName;
        this.numberOfEvents = numberOfEvents;
        this.matchingFilter = matchingFilter;
        this.timeRequested = new Date();
    }

    public String getFileName() {
        return fileName;
    }

    
    public Integer getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    public String getMatchingFilter() {
        return matchingFilter;
    }
    
    public Date getTimeRequested() {
        return timeRequested;
    }

    @Override
    public String toString() {
        return "LogCollectionRequest [fileName=" + fileName + ", matchingFilter=" + matchingFilter + ", numberOfEvents="
                + numberOfEvents + ", timeRequested=" + timeRequested + "]";
    }
    
    
}
