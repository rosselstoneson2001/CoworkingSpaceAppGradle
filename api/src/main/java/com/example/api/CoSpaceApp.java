package com.example.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.api",
        "com.example.domain",
        "com.example.services"
})
public class CoSpaceApp {

    public static void main(String[] args) {

        try {
        SpringApplication.run(CoSpaceApp.class, args);
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println(t.getMessage());
        }
    }
}