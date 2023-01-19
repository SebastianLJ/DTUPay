Feature: Customer tests

  Scenario: A customer requests new tokens
    Given a customer is in the system
    When the customer requests 3 tokens
    Then the customer receives 3 tokens

    Scenario: A customer is successfully deleted
      Given a customer is already in the system
      When a customer is deleted
      Then the customer is no longer in the system
      And the customers tokens have been deleted

  Scenario: Customer retrieves list of payments
    Given a customer is rregistered in the system
    And the customer has been involved in a payment
    When the customer retrieves a list of payments
    Then the customer can see a list of all transactions they have been involved in

  Scenario: A customer cannot retrieve a list of another customers payments
    Given two customers is registered in the system
    And customer1 has been involved in a payment
    When customer2 retrieves a list of payments
    Then the customer will not be able to see the other customers payment


