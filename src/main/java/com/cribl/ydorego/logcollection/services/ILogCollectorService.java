package com.cribl.ydorego.logcollection.services;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.model.LogCollectionRequest;
import com.cribl.ydorego.logcollection.model.LogEventsResponse;
import com.cribl.ydorego.logcollection.model.LogFilesResponse;

/**
 * Contract defining methods to be implemented by Log collectors implementors
 */
public interface ILogCollectorService {

    /**
     * Returns the list of files present in  directory.
     * 
     * @param directoryPath The directory path. For example /var/log
     * @param matchingExtensions A comma separated list of extensions to match against. For example txt,out,log
     * 
     * @return LogFilesResponse contains the list of files found.
     * @throws LogCollectorDefaultException in case of error during processing. Directory not found, etc...
     * 
     */
    LogFilesResponse getFilesFromDirectory(String directoryPath, String matchingExtensions) throws LogCollectorDefaultException;
 
    /**
     * Returns a list of Events retrieved from the specified file.
     * 
     * @param LogCollectionRequest An object encapsulating the client request, it contains the full path of the file to read,
     * the number of events to returns and a matchingFilter.
     * 
     * @return LogEventsResponse contains the list of events in reverse time order
     * @throws LogCollectorDefaultException in case of error during processing. File not found, etc...
     * 
     */
    LogEventsResponse getEventsFromFile(LogCollectionRequest logCollectionRequest) throws LogCollectorDefaultException;

}
