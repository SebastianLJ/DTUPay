Feature: Account Service

  Scenario: A merchant is successfully created
    When a merchant is created
    Then a merchant is registered in the system
    And the merchant can be found by his ID.

    Scenario: A merchant is successfully deleted
      Given a merchant is in the system
      When a merchant is deleted
      Then the merchant cannot be found