package com.cribl.ydorego.logcollection.model;

import java.util.Date;
import java.util.List;

public class LogEventsResponse {

    private final String fileName;
    private final Integer numberOfEvents;
    private final String matchingFilter;
    private final Date timeRequested;
    private final Date timeCompleted;
    private final List<String> events;
   
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

    public Integer getNumberOfEvents() {
        return numberOfEvents;
    }

    public String getMatchingFilter() {
        return matchingFilter;
    }
    
    public Date getTimeRequested() {
        return timeRequested;
    }
   
    public Date getTimeCompleted() {
        return timeCompleted;
    }

    public List<String> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "LogEventsResponse [events=" + events + ", fileName=" + fileName + ", matchingFilter=" + matchingFilter
                + ", numberOfEvents=" + numberOfEvents + ", timeCompleted=" + timeCompleted + ", timeRequested="
                + timeRequested + "]";
    }
     
    
}
