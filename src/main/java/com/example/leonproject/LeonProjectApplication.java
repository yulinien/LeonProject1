package com.example.leonproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class LeonProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeonProjectApplication.class, args);
    }

}
