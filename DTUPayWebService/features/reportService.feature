Feature: reporting

  Scenario: A merchant can retrieve a list of payments
    Given the merchant has been involved in a payment
    When a merchant retrieves a list of payments
    Then the merchant can see a list of all transactions they have been involved in

  Scenario: Customer retrieves list of payments
    Given the customer has been involved in a payment
    When the customer retrieves a list of payments
    Then the customer can see all transactions they have been involved in