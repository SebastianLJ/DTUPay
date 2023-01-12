Feature: Payment

  Scenario: Customer overdrafts their account

  Scenario: List of payments
    Given a customer with a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100 kr by the customer
    Then the payment is successful
    And the manager asks for a list of payments
    Then the list contains a payment where the customer paid "100" kr to the merchant

  Scenario: Customer is not known
    Given a merchant with a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    Given a customer with a bank account with balance 1000
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "customer is unknown"

  Scenario: Merchant is not known
    Given a customer with a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is not successful
    And an error message is returned saying "merchant is unknown"

  Scenario: Successful Payment
    Given a customer with a bank account with balance 1000
    And that the customer is registered with DTU Pay
    Given a merchant with a bank account with balance 2000
    And that the merchant is registered with DTU Pay
    When the merchant initiates a payment for 100 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 900 kr
    And the balance of the merchant at the bank is 2100 kr

