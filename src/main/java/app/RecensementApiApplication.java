package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot Recensement.
 * Démarre le serveur et scanne tous les beans (@Component, @Service, @Repository).
 */
@SpringBootApplication
public class RecensementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecensementApiApplication.class, args);
    }
}