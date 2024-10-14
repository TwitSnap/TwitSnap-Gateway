package com.example.twitsnapgateway.gatewayFilters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthFilter(RestTemplate restTemplate, String authServiceUrl) {
        super(Config.class);
        this.restTemplate = restTemplate;
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

                try {
                    // ? 2.2. Envio el token al servicio de autenticación para que lo valide. Si el token es válido no se levantara ninguna excepcion porque devolvera un 2xx.
                    ResponseEntity<String> response = restTemplate.getForEntity((authServiceUrl + System.getProperty("AUTH_MS_AUTH_PATH") + jwtToken), String.class);

                    // ? 2.2.1. Obtengo el userId del body y lo agrego al header de la request.
                    String userId;

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response.getBody());
                        userId = jsonNode.get("userId").asText();
                    } catch(Exception e) {
                        // ? Si no se pudo obtener el userId del body, entonces se setea como vacio.
                        // ? Esto se hace asi debido a que la gran mayoria de endpoints del sistema no requieren el userId asi que no vale la pena hacer que todos los endpoints
                        // ? fallen si el gateway no puede resolver el userId. Los endpoints que usen el userId deberan validar que el header userId no sea vacio.
                        userId = "";
                    }

                    exchange = addUserIdHeader(exchange, userId);
                    System.out.println("Headers: " + exchange.getRequest().getHeaders());

                    // ? 2.2.2. Y se redirige la request al destino.
                    return chain.filter(exchange);
                } catch (HttpClientErrorException e){
                    // ? 2.3. Si se levanto una excepcion porque la respuesta es de tipo 401, entonces el token no es válido y se responde con un 401.
                    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        System.out.println("Token is not valid: " + e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    } else {
                        // ? 2.4. Si la respuesta no es de tipo 401, entonces se responde con un 500.
                        System.out.println("Internal server error: " + e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    return exchange.getResponse().setComplete();
                }
            }

            // ? 3. Si el token no llego en la request entonces se responde con un 400.
            System.out.println("Token was not received");
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        };
    }
    
    private ServerWebExchange addUserIdHeader(ServerWebExchange exchange, String userId) {
            return exchange.mutate().request(r -> r.header("userId", userId)).build();
    }

    private boolean isTokenValid(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public static class Config {}
}




