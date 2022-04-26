package com.cribl.ydorego.logcollection.model;

public class LogEventsServerResponse {

    private final String serverName;
    private final LogEventsResponse logEventsResponse;
    private final int httpStatusCode;
    private final String serverMessage;


    public LogEventsServerResponse(String serverName, LogEventsResponse logEventsResponse, int httpStatusCode, String serverMessage) {
        this.serverName = serverName;
        this.logEventsResponse = logEventsResponse;
        this.httpStatusCode = httpStatusCode;
         this.serverMessage = serverMessage;
    }
    
    public String getServerName() {
        return serverName;
    }
    public LogEventsResponse getLogEventsResponse() {
        return logEventsResponse;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getServerMessage() {
        return serverMessage;
    }
    
    
}
