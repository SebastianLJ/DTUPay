# @Author Jákup Viljam Dam - s185095
Feature: Message Queue
  Scenario: Messages sent between services
    Given A message queue has been initialized
    And A Producer and Consumer have been initialized
    When A Producer sends a request to the consumer
    Then The consumer consumes the request
    Then The Producer receives a message back from the consumer