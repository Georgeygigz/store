package com.georgeygigz.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.base-url}")
    private String baseUrl;


    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server().url(baseUrl).description("Environment Base URL");
        return new OpenAPI()
                .info(new Info()
                        .title("Store API")
                        .version("1.0")
                        .description("API documentation for the Store application"))
                .servers(List.of(server));
    }
}