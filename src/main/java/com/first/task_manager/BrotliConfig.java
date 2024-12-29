package com.first.task_manager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrotliConfig {
    static {
        try {
            com.aayushatharva.brotli4j.Brotli4jLoader.ensureAvailability();
            System.out.println("Brotli native libraries loaded successfully in Spring Boot!");
        } catch (Exception e) {
            System.err.println("Failed to load Brotli native libraries in Spring Boot: " + e.getMessage());
        }
    }
}
