package com.cribl.ydorego.logcollection.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cribl.ydorego.logcollection.model.LogFilesResponse;
import com.cribl.ydorego.logcollection.services.ILogCollectorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LogCollectorController.class)
public class LogCollectorControllerGetFilesTest {
   
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ILogCollectorService logCollectorService;
 
    @MockBean
    private LogFilesResponse filesResponse;

    @Test
    public void whenPathVariableIsMissingAllParamsInvalid_thenReturnsStatus400() throws Exception {

      String expected = "{\"violations\":[{\"fieldName\":\"directoryPath\",\"message\":\"Required request parameter 'directoryPath' for method parameter type String is not present\"}]}";
      mvc.perform(get("/logCollector/get-files"))
              .andExpect(status().isBadRequest())
              .andExpect(content().json(expected));              
    } 
    
}
