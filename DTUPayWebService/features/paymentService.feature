Feature: Payment Service
  Scenario: A payment is successfully created
    Given a customer with a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100 kr by the customer
    Then a payment is registered

  Scenario: Payment does not exists
    Given the id of a new payment is queried
    Then the payment is not found

  Scenario: All payments are retrieved
    Given there exists a payment
    And there exists a payment
    When all payments are queried
    Then a list containing 2 payments are returned