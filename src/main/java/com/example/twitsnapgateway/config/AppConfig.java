package com.example.twitsnapgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.example.twitsnapgateway.gatewayFilters.AuthFilter;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthFilter authFilter(RestTemplate restTemplate) {
        // TODO: Definir en una envVar el endpoint de autenticación.
        return new AuthFilter(restTemplate, "http://localhost:5000");
    }
}
