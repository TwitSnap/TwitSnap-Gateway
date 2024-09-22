package com.example.twitsnapgateway.logFilters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A global traffic logging filter for Spring Cloud Gateway that logs:
 * 1. Incoming requests received by the gateway.
 * 2. Outgoing responses sent by the gateway.
 * 3. Requests made by the gateway to downstream services.
 * 4. Errors during the service requests.
 */
@Component
public class TrafficLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger("TrafficLogger");

    /**
     * Filter method that logs the incoming request, outgoing response, service request, and service request errors.
     * @param exchange The server web exchange object.
     * @param chain The gateway filter chain.
     * @return A mono void.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // ? Log the incoming request
        logger.info("Incoming Request -> Method: {}, URI: {}", request.getMethod(), request.getURI());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // ? Process the request and log the outgoing response
            ServerHttpResponse response = exchange.getResponse();
            logger.info("Outgoing Response -> Status code: {}", response.getStatusCode());
        })).doOnSuccess(aVoid -> {
            // ? Log the request made by the gateway to a service
            logger.info("Request to Service -> Method: {}, URI: {}", request.getMethod(), request.getURI());
        }).doOnError(throwable -> {
            // ? Log any errors that occur during service requests
            logger.error("Error during service request: {}", throwable.getMessage());
        }).then();
    }

    /**
     * Get the order of the filter.
     * @return The order of the filter.
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
