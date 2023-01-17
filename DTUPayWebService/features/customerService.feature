Feature: Customer Service

  Scenario: A customer is successfully created
    Given there is a customer with an empty id
    When a customer is being created
    Then the CustomerAccountCreated event is sent
    When the TokensGenerated event is received
    Then the customer is created
    And the customer has tokens

  Scenario: A customer is successfully deleted
    Given a customer is in the system
    When the customer is being deleted
    Then the AccountDeletionRequested event is sent
    When the TokensDeleted event is received
    Then the customer is deleted
    
  Scenario: A customer requests tokens
    Given a customer is in the system
    When the customer requests 3 tokens
    Then a tokens requested event is published