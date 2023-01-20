Feature: Merchant end-to-end

  #Nicklas Olabi (s205347)
  Scenario: A merchant is successfully created
    When a merchant is being created
    Then the merchant can be found in the system

  Scenario: A merchant is successfully deleted
    Given a merchant is already in the system
    When a merchant is being deleted
    Then the merchant is no longer in the system
