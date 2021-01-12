package com.example.tenderdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * App class
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TenderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenderDemoApplication.class, args);
    }

}
