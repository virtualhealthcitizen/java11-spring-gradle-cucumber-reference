package org.squidmin.cucumber.skeleton.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.cucumber.skeleton"
})
@Getter
@Slf4j
public class TokenValidatorServiceConfig {

    private final String baseUrl;
    private final String endpoint;

    @Autowired
    public TokenValidatorServiceConfig(@Value("${token-validator-service.base-url}") String baseUrl,
                                       @Value("${token-validator-service.validator-endpoint}") String endpoint) {

        this.baseUrl = baseUrl;
        this.endpoint = endpoint;
    }

}
