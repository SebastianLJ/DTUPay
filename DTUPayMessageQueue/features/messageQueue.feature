Feature: Message Queue
  Scenario: A simple RabbitMQ is established with events
    Given DTUPayMessageQueue has been established
    And ProducerStub and ConsumerStub are in the system
    When The ProducerStub sends a "Hello!" message via the queue
    Then The ConsumerStub receives a "Hello!" message via the queue
    When The ConsumerStub is finished modifying the message and sends it back into the queue
    Then The ProducerStub receives back a "World!" message via the queue