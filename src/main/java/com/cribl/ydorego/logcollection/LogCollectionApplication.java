package com.cribl.ydorego.logcollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cribl.ydorego.logcollection"})
public class LogCollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogCollectionApplication.class, args);
    }
    
}
