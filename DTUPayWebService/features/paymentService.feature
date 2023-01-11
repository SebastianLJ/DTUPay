Feature: Payment Service
  Scenario: A payment is successfully created
    Given there is a registered customer
    And there is a registered merchant
    When the merchant creates a payment
    And the customer approves it
    Then a payment is registered

  Scenario: Payment does not exists
    Given the id of a new payment is queried
    Then the payment is not found

  Scenario: All payments are retrieved
    Given there exists a payment
    And there exists a payment
    When all payments are queried
    Then a list containing 2 payments are returned