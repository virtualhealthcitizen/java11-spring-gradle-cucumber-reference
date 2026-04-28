package org.squidmin.cucumber.skeleton.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.squidmin.cucumber.skeleton.config.TokenValidatorServiceConfig;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenValidatorClient {

    private final TokenValidatorServiceConfig config;

    private final RestTemplate restTemplate;

    @Autowired
    public TokenValidatorClient(TokenValidatorServiceConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> validate(String token) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                config.getBaseUrl() + config.getEndpoint(),
                HttpMethod.POST,
                requestEntity,
                String.class
            );
        } catch (HttpClientErrorException e) {
            log.error("Error validating token: {}", e.getResponseBodyAsString());
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
        return responseEntity;
    }

}
