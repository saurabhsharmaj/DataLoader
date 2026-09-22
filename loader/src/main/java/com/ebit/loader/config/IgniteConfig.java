package com.ebit.loader.config;


import org.apache.ignite.client.IgniteClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {

    @Bean
    public IgniteClient igniteClient() {
        return org.apache.ignite.client.IgniteClient.builder()
                .addresses("localhost:10800")
                .build();
    }
}