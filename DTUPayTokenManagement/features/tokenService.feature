Feature: Token Service
  Scenario: A new user is given a list of tokens
    When a user is created
    Then the token list length is valid
    And the user is registered

  Scenario: Duplicate users cannot be created
    When there exists a user
    And a user is created
    Then they must have different ids