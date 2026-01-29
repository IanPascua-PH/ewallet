package com.api.ewallet.configuration;

import com.api.ewallet.configuration.properties.RestTemplateConfigProperties;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, RestTemplateConfigProperties properties) {
        return builder
            .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
            .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
            .build();
    }
}
