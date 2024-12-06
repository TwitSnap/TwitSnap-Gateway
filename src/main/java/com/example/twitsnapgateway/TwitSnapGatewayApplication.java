package com.example.twitsnapgateway;

import com.example.twitsnapgateway.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class TwitSnapGatewayApplication{
    public static void main(String[] args){
        Config.setEnv();
        SpringApplication.run(TwitSnapGatewayApplication.class, args);
    }


    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return (registry) -> registry.config().commonTags("region", "us-east-1");
    }


}
