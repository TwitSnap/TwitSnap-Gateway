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

import java.util.function.Consumer;

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
        String incomingRequestLog = String.format("Incoming Request -> Method: %s, URI: %s", request.getMethod(), request.getURI());
        log(incomingRequestLog, logger::info);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // ? Process the request and log the outgoing response
            ServerHttpResponse response = exchange.getResponse();

            String outgoingResponseLog = String.format("Outgoing Response -> Status code: %s", response.getStatusCode());
            log(outgoingResponseLog, logger::info);
        })).doOnSuccess(aVoid -> {
            // ? Log the request made by the gateway to a service
            String requestToServiceLog = String.format("Request sent to Service -> Method: %s, URI: %s", request.getMethod(), request.getURI());
            log(requestToServiceLog, logger::info);
        }).doOnError(throwable -> {
            // ? Log any errors that occur during service requests
            String errorLog = String.format("Error during service request: %s", throwable.getMessage());
            log(errorLog, logger::error);
        }).then();
    }

    /**
     * Log a message using a provided lambda for the log type, and also print to stdout.
     *
     * @param logString The message to log.
     * @param logAction A lambda function that specifies the type of log (e.g., info, error).
     */
    private void log(String logString, Consumer<String> logAction) {
        logAction.accept(logString);
        System.out.println(logString);
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
