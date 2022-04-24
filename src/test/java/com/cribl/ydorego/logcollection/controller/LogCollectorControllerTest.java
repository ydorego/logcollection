package com.cribl.ydorego.logcollection.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LogCollectorController.class)
public class LogCollectorControllerTest {
   
    @Autowired
    private MockMvc mvc;

    @Test
    public void whenPathVariableIsMissingAllParamsInvalid_thenReturnsStatus400() throws Exception {

      String expected = "{'violations':[{\"fieldName\":\"fileName\",\"message\":\"Required request parameter 'fileName' for method parameter type String is not present\"}]}";
      mvc.perform(get("/logCollector/get-events"))
              .andExpect(status().isBadRequest())
              .andExpect(content().json(expected));              
    } 
    
    @Test
    public void whenPathVariableIsMissingNumberOfEventsInvalid_thenReturnsStatus400() throws Exception {

      String expected = "{'violations':[{\"fieldName\":\"numberOfEvents\",\"message\":\"Required request parameter 'numberOfEvents' for method parameter type Integer is not present\"}]}";
      mvc.perform(get("/logCollector/get-events?fileName=testFile.txt"))
              .andExpect(status().isBadRequest())
              .andExpect(content().json(expected));              
    }    

    @Test
    public void whenPathVariableIsNegativeNumberOfEvetnsInvalid_thenReturnsStatus400() throws Exception {

        String expected = "{\"violations\":[{\"fieldName\":\"getEvents.numberOfEvents\",\"message\":\"must be greater than or equal to 1\"}]}";
        mvc.perform(get("/logCollector/get-events?fileName=testFile.txt&numberOfEvents=-1"))
              .andExpect(status().isBadRequest())
              .andExpect(content().json(expected));              
    }    

    @Test
    public void whenPathVariableIsGreaterThanMaxNumberOfEventsInvalid_thenReturnsStatus400() throws Exception {

        String expected = "{\"violations\":[{\"fieldName\":\"getEvents.numberOfEvents\",\"message\":\"must be less than or equal to 250\"}]}";
        mvc.perform(get("/logCollector/get-events?fileName=testFile.txt&numberOfEvents=1000"))
              .andExpect(status().isBadRequest())
              .andExpect(content().json(expected));              
    }    

    @Test
    public void whenAllParamsAreValid_thenReturnsStatus202() throws Exception {
      mvc.perform(get("/logCollector/get-events?fileName=testFile.txt&numberOfEvents=200"))
              .andExpect(status().isAccepted());              
    }    

    @Test
    public void whenFilterIsProvidedAreVaid_thenReturnsStatus202() throws Exception {

        String responseJsonString = mvc
                .perform(get("/logCollector/get-events?fileName=testFile.txt&numberOfEvents=200&matchingFilter=test"))
                .andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString();

        assertTrue(responseJsonString.contains("\"matchingFilter\":\"test\""));

    }
}
