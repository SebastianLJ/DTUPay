Feature: Message Queue
  Scenario: A simple RabbitMQ is established with events
    Given DTUPayMessageQueue has been established
    And ProducerStub and ConsumerStub are in the system
    When The ProducerStub sends a message via the queue
    Then The ConsumerStub receives the message via the queue
    When The ConsumerStub is finished doing work, the message is sent back into the queue
    Then The ProducerStub receives the message again via the queue