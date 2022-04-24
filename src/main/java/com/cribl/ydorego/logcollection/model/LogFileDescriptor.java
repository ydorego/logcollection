package com.cribl.ydorego.logcollection.model;

import java.util.Date;

/**
 * LogFileDescriptor metadat associated to a file.
 * 
 */
public class LogFileDescriptor {

    private final String fileName;
    private final boolean isDirectory;
    private final long length;
    private final Date lastModified;

   
    public LogFileDescriptor(String fileName, boolean isDirectory, long length, Date lastModified) {
        this.fileName = fileName;
        this.isDirectory = isDirectory;
        this.length = length;
        this.lastModified = lastModified;
     }

    public String getFileName() {
        return fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getLength() {
        return length;
    }

    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return "LogFilesResponse [fileName=" + fileName + ", isDirectory=" + isDirectory + ", lastModified="
                + lastModified + ", length=" + length + "]";
    }    
}
