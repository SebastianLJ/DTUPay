Feature: reporting

  Scenario: A merchant can retrieve a list of payments
    Given the merchant has been involved in a payment
    When a merchant retrieves a list of payments
    Then the merchant can see a list of all transactions they have been involved in







