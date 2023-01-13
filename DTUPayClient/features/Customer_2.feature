Feature: Customer tests

  Scenario: A customer requests new tokens
    Given a customer is in the system
    When the customer requests 3 tokens
    Then the customer receives 3 tokens