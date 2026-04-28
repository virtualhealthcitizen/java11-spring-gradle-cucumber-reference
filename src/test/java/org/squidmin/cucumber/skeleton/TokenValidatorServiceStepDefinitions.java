package org.squidmin.cucumber.skeleton;

import io.cucumber.java.en.Given;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TokenValidatorServiceStepDefinitions extends SpringIntegrationTest {

    @Given("the token {string} exists")
    public void the_token_exists(String token) {
        Assertions.assertNotNull(token);
        if ("valid_token".equals(token)) {
            String validToken = gcpTokenService.generateAccessToken();
            Assertions.assertNotNull(validToken);
            testContext.put("token", validToken);
        } else {
            Assertions.assertNotNull(token);
            testContext.put("token", token);
        }
    }

    @Given("I validate the token")
    public void i_validate_the_token() {
        Object token = testContext.getOrDefault("token", null);
        Assertions.assertInstanceOf(String.class, token);
        testContext.put("responseEntity", tokenValidatorClient.validate((String) token));
    }

    @Given("I receive a valid response")
    public void i_receive_a_valid_response() {
        Object obj = testContext.getOrDefault("responseEntity", null);
        Assertions.assertNotNull(obj);
        Assertions.assertInstanceOf(ResponseEntity.class, obj);
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) obj;
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertInstanceOf(String.class, responseEntity.getBody());
        String responseBody = (String) responseEntity.getBody();
        HttpStatus responseStatusCode = responseEntity.getStatusCode();
        Assertions.assertTrue(responseStatusCode.is2xxSuccessful() || responseStatusCode.is4xxClientError());
        if (responseStatusCode.is2xxSuccessful()) {
            Assertions.assertTrue(responseBody.contains("token_info"));
        } else {
            Assertions.assertTrue(responseBody.contains("Invalid or expired token"));
        }
    }

}
