package com.matchpuff.matchingservice.matching_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
        .components(new Components().addSecuritySchemes(BEARER_AUTH,
            new SecurityScheme()
                .name(BEARER_AUTH)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
                .info(new Info()
                        .title("Matching Service API")
                        .version("1.0.0")
                        .description("Documentación de los endpoints del Matching Service"));
    }

}
