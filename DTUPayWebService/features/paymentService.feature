Feature: Payment Service
  Scenario: A payment is successfully created
    Given there is a registered customer
    And there is a registered merchant
    When the merchant creates a payment
    And the customer approves it
    Then a payment is registered