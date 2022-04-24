package com.cribl.ydorego.logcollection.services;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.model.LogFilesResponse;

/**
 * Contract defining methods to be implemented by Log collectors implementors
 */
public interface ILogCollectorService {

    /**
     * Returns the list of files present in  directory
     * @param directoryPath The directory path. For example /var/log
     * @param matchingExtensions A comma separated list of extensions to match against. For example txt,out,log
     * 
     * @return LogFilesResponse containing the list of files found.
     * @throws LogCollectorDefaultException in case of error during processing. Directory not found, etc...
     * 
     */
    LogFilesResponse getFilesFromDirectory(String directoryPath, String matchingExtensions) throws LogCollectorDefaultException;
    
}
