Feature: Customer Service
  Scenario: A customer is successfully created
    When a customer is created
    Then he is registered in the system
    And can be found by his ID