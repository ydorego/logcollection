package com.cribl.ydorego.logcollection.services;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.services.filter.ContainsAllFilter;
import com.cribl.ydorego.logcollection.services.filter.ContainsStringFilter;
import com.cribl.ydorego.logcollection.services.filter.IEventFilter;
import com.cribl.ydorego.logcollection.services.filter.RegexFilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class LogCollectorServiceImplTest {

    private LogCollectorServiceImpl testInstance;
    
    @Test
    void testName() {
        
    }

    @BeforeEach
    void beforeClass() {
        testInstance =  new LogCollectorServiceImpl();
    }

    @Test
    void testGetEventFilterBadScheme() {

        LogCollectorDefaultException thrown = Assertions.assertThrows(LogCollectorDefaultException.class, () -> {
            testInstance.getEventFilter("bad-scheme:");
        });
        Assertions.assertEquals("Unsupported filter scheme or format for filter:" + "bad-scheme:", thrown.getMessage());
    }

    @Test
    void testGetEventFilterAllSchemes() throws LogCollectorDefaultException {
        IEventFilter filter = testInstance.getEventFilter("contains-all:");
        Assertions.assertTrue(filter instanceof ContainsAllFilter);

        filter = testInstance.getEventFilter("contains:");
        Assertions.assertTrue(filter instanceof ContainsStringFilter);

        filter = testInstance.getEventFilter("regex:");
        Assertions.assertTrue(filter instanceof RegexFilter);

    }    
}
