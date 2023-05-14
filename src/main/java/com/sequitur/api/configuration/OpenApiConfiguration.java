package com.sequitur.api.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class OpenApiConfiguration {
    @Bean(name = "sequiturOpenApi")
    public OpenAPI sequiturOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Sequitur Application API")
                        .description("Sequitur API implemented with Spring Boot RESTful service and documented using springdoc-openapi and OpenAPI 3.0"));
    }

}
