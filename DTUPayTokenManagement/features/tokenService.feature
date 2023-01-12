Feature: Token Service
  Scenario: A new user is given a list of tokens
    When a user is created
    Then the token list length is valid
    And the user is registered

  Scenario: Duplicate users cannot be created
    When a user is created
    And a second is created
    Then they must have different ids
    And they must have different tokens