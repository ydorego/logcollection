package com.cribl.ydorego.logcollection.model;

public class LogEventsServerResponse {

    private final String serverName;
    private final LogEventsResponse logEventsResponse;

    
    public LogEventsServerResponse(String serverName, LogEventsResponse logEventsResponse) {
        this.serverName = serverName;
        this.logEventsResponse = logEventsResponse;
    }
    
    public String getServerName() {
        return serverName;
    }
    public LogEventsResponse getLogEventsResponse() {
        return logEventsResponse;
    }
    
    
    
}
