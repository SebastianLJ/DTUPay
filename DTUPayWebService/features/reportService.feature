Feature: reporting

  Scenario: A merchant retrieves a list of payments
    Given a merchant is rregistered in the system
    And the merchant has been involved in a payment
    When a merchant retrieves a list of payments
    Then the merchant can see a list of all transactions they have been involved in

  Scenario: Customer retrieves list of payments
    Given a customer is rregistered in the system
    And the customer has been involved in a payment
    When the customer retrieves a list of payments
    Then the customer can see a list of all transactions they have been involved in
