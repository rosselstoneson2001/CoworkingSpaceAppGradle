package com.example.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example",
        "com.example.security",
        "com.example.services"
})
public class CoSpaceApp {

    public static void main(String[] args) {
        SpringApplication.run(CoSpaceApp.class, args);
    }
}