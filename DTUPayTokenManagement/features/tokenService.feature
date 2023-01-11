Feature: Token Service
  Scenario: A user is given a list of tokens
    When a user is created
    Then the generated token list has 6 tokens
    And the user is registered