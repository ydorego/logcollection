package com.cribl.ydorego.logcollection.services;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.model.LogCollectionRequest;
import com.cribl.ydorego.logcollection.model.LogEventsResponse;
import com.cribl.ydorego.logcollection.model.LogEventsServerResponse;
import com.cribl.ydorego.logcollection.model.LogFileDescriptor;
import com.cribl.ydorego.logcollection.model.LogFilesResponse;

@Service
public class LogCollectorServiceImpl implements ILogCollectorService {

    Logger log = LoggerFactory.getLogger(LogCollectorServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    // public LogCollectorServiceImpl(@Autowired RestTemplateBuilder builder) {
    //    this.restTemplate = builder.build();
    // }
 
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

            // If filter is defined use it otherwise returns all....
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

    /**
     * 
     * Implementation of is using backtracking to handle the reverse order requirement. The upperbound
     * in the total number of events to return will ensure that we are not running the rick of running
     * out of memory when reading a finite number of events.
     * 
     */
    @Override
    public LogEventsResponse getEventsFromFile(@NotNull LogCollectionRequest logCollectionRequest)
            throws LogCollectorDefaultException {

        File file = new File(logCollectionRequest.getFileName());
                
        StringBuilder currentLine = new StringBuilder();
        List<String> linesToReturn = new ArrayList<>();

        RandomAccessFile logFileToProcess = null;
        try {

            /*
             Random access file, opening in read mode and given the total
             length of the file, we position the offset at the very end of the file
            */
            logFileToProcess = new RandomAccessFile(file, "r");
            long fileLength = file.length() - 1;

            // Set the offset at the last position in the file
            logFileToProcess.seek(fileLength);

            // Start reading the request number of lines
            //
            long currentOffset = fileLength;
            while (linesToReturn.size() < logCollectionRequest.getNumberOfEvents() && currentOffset >= 0) {

                currentLine.setLength(0);

                // Process a line, we consider that the end of the line is the carriage return
                //
                while (currentOffset >= 0) {

                    logFileToProcess.seek(currentOffset);
                    char currentChar = (char) logFileToProcess.read();

                    // break when end of the line, meaning beginning of the previous line
                    if (currentChar == '\n') {
                        /*
                         We are backtracking, need to reverse the line that we've just read and add it
                         to the total that we have to return
                        */
                        // TODO: Remember the filtering case...
                        //
                        linesToReturn.add(currentLine.reverse().toString());

                        // Skip the previous line carriage return
                        currentOffset--;
                        currentLine.setLength(0);
                        break;
                    }
                    currentLine.append(currentChar);
                    currentOffset--;
                }

                // Let's add the last line that was read.
                if (currentOffset < 0) {
                    // TODO: Remember the filtering case...
                    //
                    linesToReturn.add(currentLine.reverse().toString());
                }
            }

            log.info("* -------------------------------------------------------- *");
            linesToReturn.stream().forEach(s -> log.info(s));
            log.info("* -------------------------------------------------------- *");

            return new LogEventsResponse(
                logCollectionRequest.getFileName(), 
                logCollectionRequest.getNumberOfEvents(), 
                logCollectionRequest.getMatchingFilter(), 
                logCollectionRequest.getTimeRequested(), 
                new Date(),
                linesToReturn);

        } catch (FileNotFoundException e) {
            throw new LogCollectorDefaultException(logCollectionRequest.getFileName(), "File " + logCollectionRequest.getFileName() + " does not exist");          
        } catch (IOException e) {
            throw new LogCollectorDefaultException(logCollectionRequest.getFileName(), "IO Exception during processing of " + logCollectionRequest.getFileName() + " file");          
        } finally {
            if(logFileToProcess != null){
                try {
                    logFileToProcess.close();
                } catch (IOException e) {
                    throw new LogCollectorDefaultException(logCollectionRequest.getFileName(), "IO Exception during processing of " + logCollectionRequest.getFileName() + " file");          
                }
            }
        }               
    }

    /**
     * Reuse of getEventsFromFile to retrieve fro remote servers. The current implementation is not optimal since we are
     * processing sequentially. 
     * 
     * It could be improved with a thread pool forking the request in parallel and collecting results asynchronously.
     * 
     * TODO: Error Handling.
     * 
     */
    @Override
    public List<LogEventsServerResponse> getEventsFromFileFromServers(LogCollectionRequest logCollectionRequest) throws LogCollectorDefaultException {
 
        String[] serverList = logCollectionRequest.getServerList().split(",");

        StringBuilder query = new StringBuilder(":8090/logCollector/get-events?");
        query.append("fileName=" + logCollectionRequest.getFileName());
        query.append("&numberOfEvents=" + logCollectionRequest.getNumberOfEvents());
        if (logCollectionRequest.getMatchingFilter() != null) {
            query.append("&matchingFilter=" + logCollectionRequest.getMatchingFilter());
        }
      
        List<LogEventsServerResponse> serversResponses = new ArrayList<>();
        Arrays.asList(serverList).stream().forEach(server -> {
            LogEventsResponse responseFromServer = restTemplate.getForObject("http://" + server + query, LogEventsResponse.class);
            serversResponses.add(new LogEventsServerResponse(server, responseFromServer));
        });

        return serversResponses;
    }
}
