package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ╔══════════════════════════════════════════╗
 * ║     Banking & Finance REST API           ║
 * ║     Built with Spring Boot 3 + JWT       ║
 * ╚══════════════════════════════════════════╝
 *
 * Swagger UI  → http://localhost:8080/swagger-ui.html
 * H2 Console  → http://localhost:8080/h2-console
 * API Docs    → http://localhost:8080/api-docs
 */
@SpringBootApplication
public class BankingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApiApplication.class, args);

        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║       Banking API Started Successfully!          ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Swagger UI  : http://localhost:8080/swagger-ui.html  ║");
        System.out.println("║  H2 Console  : http://localhost:8080/h2-console       ║");
        System.out.println("║  API Docs    : http://localhost:8080/api-docs          ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");
    }
}
