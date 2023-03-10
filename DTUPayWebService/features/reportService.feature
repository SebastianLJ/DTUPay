Feature: reporting

  #Sebastian Juste Pedersen (s205335)
  #Nicklas Olabi (s205347)
  Scenario: A merchant retrieves a list of payments
    Given a merchant is rregistered in the system
    And the merchant has been involved in a payment
    When a merchant retrieves a list of payments
    Then the merchant can see a list of all transactions they have been involved in

  Scenario: A merchant cannot retrieve a list of another merchants payments
    Given two merchants is registered in the system
    And merchant1 has been involved in a payment
    When merchant2 retrieves a list of payments
    Then the merchant will not be able to see the other merchants payment

  Scenario: Customer retrieves list of payments
    Given a customer is rregistered in the system
    And the customer has been involved in a payment
    When the customer retrieves a list of payments
    Then the customer can see a list of all transactions they have been involved in
