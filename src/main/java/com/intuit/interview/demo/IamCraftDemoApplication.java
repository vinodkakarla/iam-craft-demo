package com.intuit.interview.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * This is the main class for the Spring Boot application.
 * It uses the @SpringBootApplication annotation to indicate that it is a Spring Boot application.
 * It also uses the @EnableCaching annotation to enable caching in the application.
 */
@SpringBootApplication
@EnableCaching
public class IamCraftDemoApplication {

    /**
     * This is the main method that starts the Spring Boot application.
     * It uses the SpringApplication.run method to launch the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(IamCraftDemoApplication.class, args);
    }

}