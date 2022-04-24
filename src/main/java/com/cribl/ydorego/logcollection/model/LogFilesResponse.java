package com.cribl.ydorego.logcollection.model;

import java.util.List;

/**
 * LogFilesResponse contains list of files returns from a get-files request.
 * 
 */
public class LogFilesResponse {

    private final String directoryPath;
    private final String matchingExtensions;
    private final List<LogFileDescriptor> files;
   
    public LogFilesResponse(String directoryPath, String matchingExtensions, List<LogFileDescriptor> files) {
        this.directoryPath = directoryPath;
        this.matchingExtensions = matchingExtensions;
        this.files = files;
     }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getMatchingExtensions() {
        return matchingExtensions;
    }

    public List<LogFileDescriptor> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "LogFilesResponse [directoryPath=" + directoryPath + ", files=" + files + ", matchingExtensions="
                + matchingExtensions + "]";
    }
     
}
