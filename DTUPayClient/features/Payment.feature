#Sebastian Lund (s184209)
Feature: Payment end-to-end

  Scenario: A payment is completed
    Given a merchant has a bank account with a balance of 1000
    And the merchant is a member of DTUPay
    Given a customer has a bank account with a balance of 2000
    And the customer is a member of DTUPay
    And the customer has at least one valid token
    When the merchant initializes a payment of 500
    And the customer shares a token with the merchant
    Then a payment can be done
    Then The customer's bank account balance is now 1500
    And The merchant's bank account balance is now 1500

  Scenario: A customer tries to pay with an unknown token
    Given a merchant has a bank account with a balance of 1000
    And the merchant is a member of DTUPay
    Given a customer has a bank account with a balance of 2000
    And the customer is a member of DTUPay
    And the customer has an unknown token
    When the merchant initializes a payment of 500
    And the customer shares a token with the merchant
    Then the payment is rejected with "code: 400 message: token is invalid"

  Scenario: The customer does not have a bank account associated
    Given a merchant has a bank account with a balance of 1000
    And the merchant is a member of DTUPay
    And the customer is a member of DTUPay
    And the customer has at least one valid token
    When the merchant initializes a payment of 500
    And the customer shares a token with the merchant
    Then the payment is rejected with "code: 400 message: Debtor account does not exist"

  Scenario: The merchant does not have a bank account associated
    Given the merchant is a member of DTUPay
    And a customer has a bank account with a balance of 2000
    And the customer is a member of DTUPay
    And the customer has at least one valid token
    When the merchant initializes a payment of 500
    And the customer shares a token with the merchant
    Then the payment is rejected with "code: 400 message: Creditor account does not exist"

  Scenario: The merchant is not registered with DTUPay
    Given the merchant is not a member of DTUPay
    And a customer has a bank account with a balance of 2000
    And the customer is a member of DTUPay
    And the customer has at least one valid token
    When the merchant initializes a payment of 500
    And the customer shares a token with the merchant
    Then the payment is rejected with "code: 400 message: merchant is unknown"