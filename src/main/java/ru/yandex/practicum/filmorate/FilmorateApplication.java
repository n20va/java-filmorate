package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FilmorateApplication {
    
    public static void main(String[] args) {
        log.info("=== STARTING FILMORATE APPLICATION ===");
        try {
            SpringApplication.run(FilmorateApplication.class, args);
            log.info("=== FILMORATE APPLICATION STARTED SUCCESSFULLY ===");
        } catch (Exception e) {
            log.error("=== FILMORATE APPLICATION FAILED TO START ===", e);
            throw e;
        }
    }
}
