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
    @Max(500)
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

    public String getMatchingFilter() {
        return matchingFilter;
    }
 
    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
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
