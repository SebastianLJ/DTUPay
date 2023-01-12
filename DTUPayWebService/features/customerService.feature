Feature: Customer Service
  Scenario: A customer is successfully created
    When a customer is created
    Then he is registered in the system
    And can be found by his ID

  Scenario: A customer is successfully deleted
    Given a customer is in the system
    When a customer is deleted
    Then the customer cannot be found