Feature: Token validator service
    Testing Cucumber / Spring integration with token validator service

    Scenario Outline: Validate a token
        Given the token "<token>" exists
        When I validate the token
        Then I receive a valid response

        Examples:
          | token             |
          | valid_token       |
          | invalid_token_456 |
