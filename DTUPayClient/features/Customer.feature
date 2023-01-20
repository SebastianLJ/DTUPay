Feature: Customer end-to-end
  #Sebastian Lund (s184209)
  Scenario: A customer requests new tokens
    Given a customer is in the system
    When the customer requests 3 tokens
    Then the customer receives 3 tokens

    #Sebastian Juste pedersen (s205335)
  Scenario: A customer is successfully deleted
    Given a customer is already in the system
    When a customer is deleted
    Then the customer is no longer in the system
    And the customers tokens have been deleted



