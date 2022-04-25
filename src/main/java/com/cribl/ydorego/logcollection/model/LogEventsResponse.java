package com.cribl.ydorego.logcollection.model;

import java.util.Date;
import java.util.List;

/**
 * LogEventsResponse list of evetns retruns as part of a request to get events from a file.
 * 
 */
public class LogEventsResponse {

    private String fileName;
    private Integer numberOfEvents;
    private String matchingFilter;
    private Date timeRequested;
    private Date timeCompleted;
    private List<String> events;
 
    public LogEventsResponse() {
    }

    public LogEventsResponse(String fileName, Integer numberOfEvents, String matchingFilter, Date timeRequested, Date timeCompleted, List<String> events) {
        this.fileName = fileName;
        this.numberOfEvents = numberOfEvents;
        this.matchingFilter = matchingFilter;
        this.timeRequested = timeRequested;
        this.timeCompleted = timeCompleted;
        this.events = events;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(Integer numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public String getMatchingFilter() {
        return matchingFilter;
    }

    public void setMatchingFilter(String matchingFilter) {
        this.matchingFilter = matchingFilter;
    }

    public Date getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    public Date getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(Date timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "LogEventsResponse [events=" + events + ", fileName=" + fileName + ", matchingFilter=" + matchingFilter
                + ", numberOfEvents=" + numberOfEvents + ", timeCompleted=" + timeCompleted + ", timeRequested="
                + timeRequested + "]";
    }
     
    
}
