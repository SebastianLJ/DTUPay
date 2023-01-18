Feature: reporting

  Scenario: Customer retrieves list of payments
    Given the customer has been involved in a payment
    When the customer retrieves a list of payments
    Then the customer can see all transactions they have been involved in