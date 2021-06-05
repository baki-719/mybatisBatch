package com.example.mybatisbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class MybatisBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisBatchApplication.class, args);
    }

}
