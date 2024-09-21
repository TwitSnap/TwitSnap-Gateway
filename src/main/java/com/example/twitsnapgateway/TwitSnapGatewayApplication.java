package com.example.twitsnapgateway;

import com.example.twitsnapgateway.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitSnapGatewayApplication{
    public static void main(String[] args){
        Config.setEnv();
        SpringApplication.run(TwitSnapGatewayApplication.class, args);
    }
}
