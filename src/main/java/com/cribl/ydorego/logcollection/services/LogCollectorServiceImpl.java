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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.model.LogCollectionRequest;
import com.cribl.ydorego.logcollection.model.LogEventsResponse;
import com.cribl.ydorego.logcollection.model.LogEventsServerResponse;
import com.cribl.ydorego.logcollection.model.LogFileDescriptor;
import com.cribl.ydorego.logcollection.model.LogFilesResponse;
import com.cribl.ydorego.logcollection.services.filter.ContainsAllFilter;
import com.cribl.ydorego.logcollection.services.filter.ContainsStringFilter;
import com.cribl.ydorego.logcollection.services.filter.IEventFilter;
import com.cribl.ydorego.logcollection.services.filter.RegexFilter;

@Service
public class LogCollectorServiceImpl implements ILogCollectorService {

    Logger log = LoggerFactory.getLogger(LogCollectorServiceImpl.class);

    @Value("${server.port}")
    private String serverPort;
    
    @Autowired
    private RestTemplate restTemplate;
 
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

        //
        // Validate Filter if provided...
        //
        IEventFilter eventFilter = null;
        if (logCollectionRequest.getFilter() != null && !logCollectionRequest.getFilter().isEmpty()) {
            eventFilter = getEventFilter(logCollectionRequest.getFilter());
        }
       
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
                        String currentEvent = currentLine.reverse().toString();
                        if (eventFilter == null || eventFilter.accept(currentEvent)) {
                            linesToReturn.add(currentEvent);
                        }

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
                    String currentEvent = currentLine.reverse().toString();
                    if (eventFilter == null || eventFilter.accept(currentEvent)) {
                        linesToReturn.add(currentEvent);
                    }
                }
            }

            log.info("* -------------------------------------------------------- *");
            linesToReturn.stream().forEach(s -> log.info(s));
            log.info("* -------------------------------------------------------- *");

            return new LogEventsResponse(
                logCollectionRequest.getFileName(), 
                logCollectionRequest.getNumberOfEvents(), 
                logCollectionRequest.getFilter(), 
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
    */
    @Override
    public List<LogEventsServerResponse> getEventsFromFileFromServers(LogCollectionRequest logCollectionRequest) throws LogCollectorDefaultException {
 
        String[] serverList = logCollectionRequest.getServerList().split(",");

        StringBuilder query = new StringBuilder(":" + serverPort + "/logCollector/get-events?");
        query.append("fileName=" + logCollectionRequest.getFileName());
        query.append("&numberOfEvents=" + logCollectionRequest.getNumberOfEvents());
        if (logCollectionRequest.getFilter() != null) {
            query.append("&filter=" + logCollectionRequest.getFilter());
        }
      
        List<LogEventsServerResponse> serversResponses = new ArrayList<>();
        Arrays.asList(serverList).stream().forEach(server -> {
            try {
                ResponseEntity<LogEventsResponse> responseFromServer = restTemplate.getForEntity("http://" + server + query, LogEventsResponse.class);
                serversResponses.add(new LogEventsServerResponse(server, responseFromServer.getBody(), responseFromServer.getStatusCodeValue(), ""));
            } catch (RestClientException restException) {
                serversResponses.add(new LogEventsServerResponse(server, null, 400, restException.getMessage()));
            }
        });

        return serversResponses;
    }

    /**
     * 
     * Could have use fancy SpringBoot injection, but went with plain switch statement.
     * 
     * @return Filter corresponding to the user selected scheme.
     * 
     * @throws LogCollectorDefaultException
     * 
     */
    protected IEventFilter getEventFilter(String filter) throws LogCollectorDefaultException {

        try {
            String scheme = filter.substring(0, filter.indexOf(":"));

            IEventFilter eventFilter = null;
            switch (scheme) {

                case "contains-all": {
                    eventFilter = new ContainsAllFilter(filter);
                }
                    break;

                case "contains": {
                    eventFilter = new ContainsStringFilter(filter);
                }
                    break;

                case "regex": {
                    eventFilter = new RegexFilter(filter);
                }
                    break;

                default: {
                    throw new LogCollectorDefaultException(filter, "Unsupported filter scheme or format for filter:" + filter);
                }
            }
            return eventFilter;
        } catch (StringIndexOutOfBoundsException boundEx) {
            throw new LogCollectorDefaultException(filter,  "Unsupported filter scheme or format for filter:" + filter);
        }
        
    }
}