Feature: Customer Service

  Scenario: A customer is successfully created
    When a customer is created
    Then he is registered in the system
    And can be found by his ID


  Scenario: A customer has a bank account
    Given a customer is in the system
    Then the customer has a bank account

    Scenario: A customer is successfully deleted
      Given a customer is in the system
      When the customer is being deleted
      Then the AccountDeletionRequested event is sent
      When the TokensDeleted event is received
      Then the customer is deleted
