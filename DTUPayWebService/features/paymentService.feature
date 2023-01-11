Feature: Payment Service
  Scenario: A payment is successfully created
    Given there is a registered customer
    And there is a registered merchant
    When the merchant creates a payment
    And the customer approves it
    Then a payment is registered

  Scenario: A payment is successfully found
    Given there exists a payment
    When the id of the payment is queried
    Then the payment is found

  Scenario: Payment does not exists
    Given the id of a new payment is queried
    Then the payment is not found