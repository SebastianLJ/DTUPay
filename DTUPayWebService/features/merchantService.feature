Feature: Merchant Service

  # @Author Jákup, Noah & Sebastion (Mob programming)
  Scenario: A successful payment has been created
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

  # @Author Jákup, Noah & Sebastion (Mob programming)
  Scenario: Customer tries to pay with invalid token
    Given a merchant has a bank account with a balance of 1000
    And the merchant is a member of DTUPay
    Given a customer has a bank account with a balance of 2000
    And the customer is a member of DTUPay
    And the customer has one invalid token
    When the merchant initializes a payment of 500
    And the customer shares a token with the merchant
    Then the payment fails

  # @Author Noah Christiansen (s184186)
  Scenario: A merchant is successfully created
    When a merchant is created
    Then a merchant is registered in the system
    And the merchant can be found by his ID.

  # @Author Noah Christiansen (s184186)
  Scenario: A merchant is successfully deleted
    Given a merchant is in the system
    When a merchant is deleted
    Then the merchant cannot be found

  Scenario: A merchant has a bank account
    Given a merchant is in the system
    Then the merchant has a bank account
