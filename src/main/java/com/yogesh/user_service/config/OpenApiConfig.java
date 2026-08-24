package com.yogesh.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management Service API")
                        .description(
                                "REST API for managing users with CRUD operations, " +
                                        "search, pagination, sorting, validation and global exception handling."
                        )
                        .version("1.0.0"));
    }
}
