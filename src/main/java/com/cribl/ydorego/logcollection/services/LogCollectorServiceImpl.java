package com.cribl.ydorego.logcollection.services;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.model.LogFileDescriptor;
import com.cribl.ydorego.logcollection.model.LogFilesResponse;

@Service
public class LogCollectorServiceImpl implements ILogCollectorService {

    Logger log = LoggerFactory.getLogger(LogCollectorServiceImpl.class);

    @Override
    public LogFilesResponse getFilesFromDirectory(String directoryPath, String matchingExtensions) throws LogCollectorDefaultException {

        List<LogFileDescriptor> logFileDescriptors = new ArrayList<>();

		try {
			File directory = new File(directoryPath);

			// Create a FileFilter to only accept files with comma separated extensions.
            // Using simple regex internally not to expose complexity to the end user.
            //
            FileFilter fileFilter = null;
            if (matchingExtensions != null && !matchingExtensions.isEmpty()) {
                String extensionsToMatch = matchingExtensions.replace(",", "|").replace(" ", "").trim();
                Pattern fileExtnPtrn = Pattern.compile("([^\\s]+(\\.(?i)(" + extensionsToMatch + "))$)");
                fileFilter = new FileFilter() {               
                    public boolean accept(File file) {
                        Matcher match = fileExtnPtrn.matcher(file.getName());
                        if (match.matches()) {
                            return true;
                        }
                        return false;
                    }
                };    
            }

            // If fielter is sdefined use it otherwise returns all....
			File[] files = fileFilter != null ? directory.listFiles(fileFilter) : directory.listFiles();            
            if (files == null) {
                throw new LogCollectorDefaultException(directoryPath, "Directory " + directoryPath + " does not exist");
            }

            List<File> listOfFiles = Arrays.asList(files);
            listOfFiles.stream().forEach(file -> {
                LogFileDescriptor newEntry = new LogFileDescriptor(file.getAbsolutePath(), file.isDirectory(), file.length(), new Date(file.lastModified()));
                logFileDescriptors.add(newEntry);
            });
			log.debug("Files are:{}", logFileDescriptors);
		} catch (NullPointerException ex) {           
            throw new LogCollectorDefaultException(directoryPath, "Error during files directory listing for " + directoryPath, ex);
		}
        return new LogFilesResponse(directoryPath, matchingExtensions, logFileDescriptors);
    }
    
}
