package com.jorge.ecommerce.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce REST API",
                description = "Basic E-commerce RESTful API"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
        @Bean
        public GroupedOpenApi v1Api() {
                String[] paths = {"/api/v1/**"};
                return GroupedOpenApi.builder()
                        .group("v1")
                        .pathsToMatch(paths)
                        .build();
        }
        @Bean
        public GroupedOpenApi v2Api() {
                String[] paths = {"/api/v2/**"};
                return GroupedOpenApi.builder()
                        .group("v2")
                        .pathsToMatch(paths)
                        .build();
        }
}
