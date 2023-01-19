# @author Alexander Faarup Christensen - s174355
Feature: Token Service
  Scenario: A new user is given a list of tokens
    When a message queue is started
    And a new user is created
    Then the user has a valid amount of tokens

  Scenario: A user cannot have duplicate tokens
    When a message queue is started
    And a new user is created
    Then all tokens are unique

  Scenario: The system can return a history of consumed tokens for a user
    When a message queue is started
    And a new user is created
    And the new user consumes tokens
    Then he can get a list of the consumed tokens

  Scenario: User cannot consume the same token multiple times
    When a message queue is started
    And a new user is created
    Then the user cannot consume the same token twice

  Scenario: User cannot generate tokens when they have an invalid amount
    When a message queue is started
    And a new user is created
    And the user has more than one token
    Then the user cannot request more tokens

  Scenario: User cannot request an invalid token amount
    When a message queue is started
    And a new user is created
    Then the user cannot request an invalid amount