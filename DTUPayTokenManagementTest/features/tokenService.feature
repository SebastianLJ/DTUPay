Feature: Token Service
  Scenario: A new user is given a list of tokens
    When a message queue is started
    And a new user is created
    Then the user has a valid amount of tokens

  Scenario: Duplicate users cannot be created
    When a message queue is started
    And a new user is created
    And a second user is created
    Then they must have different ids
    And they must have different tokens

  Scenario: A user cannot have duplicate tokens
    When a message queue is started
    And a new user is created
    Then all tokens are unique
