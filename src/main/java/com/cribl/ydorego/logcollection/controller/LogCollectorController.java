package com.cribl.ydorego.logcollection.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.model.LogCollectionRequest;
import com.cribl.ydorego.logcollection.model.LogEventsResponse;
import com.cribl.ydorego.logcollection.model.LogFilesResponse;
import com.cribl.ydorego.logcollection.services.ILogCollectorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@Validated
@RequestMapping("logCollector")
public class LogCollectorController {

   Logger log = LoggerFactory.getLogger(LogCollectorController.class);

   @Autowired
   private ILogCollectorService logCollectorService;


   @GetMapping(path = "/get-files", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<LogFilesResponse> getFiles(
         @NotNull @RequestParam(required = true) String directoryPath,
         @RequestParam(required = false) String matchingExtensions) throws LogCollectorDefaultException {
            return new ResponseEntity<>(logCollectorService.getFilesFromDirectory(directoryPath, matchingExtensions), HttpStatus.ACCEPTED);
   }

   @GetMapping(path = "/get-events", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<LogEventsResponse> getEvents(
         @NotNull @RequestParam(required = true) String fileName,
         @NotNull @Min(1) @Max(250) @RequestParam(required = true) Integer numberOfEvents,
         @RequestParam(required = false) String matchingFilter) {

      List<String> events = new ArrayList<>();

      events.add("Fake Events 1");
      events.add("Fake Events 2");

      LogCollectionRequest logCollectionRequest = new LogCollectionRequest(fileName, numberOfEvents, matchingFilter);

      log.info("Received log collection request: {} and about to delegate to log collector worker",
            logCollectionRequest);
      //
      // TBD: Log Collection Service will perform actual log processing
      //

      LogEventsResponse response = new LogEventsResponse(logCollectionRequest.getFileName(),
            logCollectionRequest.getNumberOfEvents(),
            logCollectionRequest.getMatchingFilter(),
            logCollectionRequest.getTimeRequested(),
            new Date(), events);

      return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

   }

}
