package com.ing.springeventstalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringEventsTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEventsTalkApplication.class, args);
    }

}
