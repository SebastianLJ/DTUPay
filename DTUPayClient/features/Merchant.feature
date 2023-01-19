Feature: Merchant App

  Scenario: A merchant is successfully created
    When a merchant is being created
    Then the merchant can be found in the system

  Scenario: A merchant is successfully deleted
    Given a merchant is already in the system
    When a merchant is being deleted
    Then the merchant is no longer in the system

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
