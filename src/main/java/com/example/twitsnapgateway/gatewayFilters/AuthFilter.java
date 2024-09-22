package com.example.twitsnapgateway.gatewayFilters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthFilter(RestTemplate restTemplate, String authServiceUrl) {
        super(Config.class);
        this.restTemplate = restTemplate;
        // TODO: Definir en una envVar authServiceUrl.
        this.authServiceUrl = authServiceUrl;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // ? 1. Obtengo el token de la request.
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // ? 2. Si es token no es null y arranca con la keyword "Bearer ", entonces lo valido.
            if (isTokenValid(token)) {
                // ? 2.1. Obtengo el token sin la keyword "Bearer ".
                String jwtToken = token.substring(7);

                // ? 2.2. Envio el token al servicio de autenticación para que lo valide.
                // TODO: Definir en una envVar el endpoint de autenticación.
                ResponseEntity<Void> response = restTemplate.getForEntity((authServiceUrl + "/v1/auth/" + jwtToken), Void.class);

                // ? 2.3. Si la respuesta es 2xx, entonces el token es válido y se permite el acceso.
                if (response.getStatusCode().is2xxSuccessful()) {
                    return chain.filter(exchange);
                }
            }

            // ? 3. Si el token no llego en la request o no es válido, entonces se responde con un 401.
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    private boolean isTokenValid(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public static class Config {}
}




