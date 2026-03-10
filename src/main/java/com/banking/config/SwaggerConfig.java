package com.banking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * SwaggerConfig — configures OpenAPI / Swagger UI documentation.
 *
 * Visit: http://localhost:8080/swagger-ui.html
 *
 * To test protected endpoints in Swagger:
 *  1. Call POST /api/auth/login to get a token
 *  2. Click "Authorize" button (top right)
 *  3. Enter: Bearer <your-token>
 */
@OpenAPIDefinition(
        info = @Info(
                title       = "Banking & Finance REST API",
                version     = "1.0.0",
                description = "A full-featured banking REST API built with Spring Boot 3 and JWT Authentication",
                contact     = @Contact(
                        name  = "Your Name",
                        email = "your.email@example.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name         = "bearerAuth",
        description  = "JWT Authorization. Enter 'Bearer <token>'",
        scheme       = "bearer",
        type         = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in           = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    // Configuration is done via annotations above.
    // No additional beans needed for SpringDoc.
}
