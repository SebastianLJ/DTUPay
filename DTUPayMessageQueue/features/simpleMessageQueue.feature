Feature: Simple Message Queue
  Scenario: A simple RabbitMQ is established with events
    Given DTUPayMessageQueue has been established
    And ProducerStub and ConsumerStub are in the system
    And The first EventRequestedStub
    When The ProducerStub sends a "Hello!" message via the queue
    Then The ConsumerStub receives a "Hello!" message via the queue
    When The ConsumerStub is finished modifying the message and sends it back into the queue
    Then The ProducerStub receives back a "World!" message via the queue
  Scenario: A simple rabbitMQ is established with multiple published events at the same time
    Given DTUPayMessageQueue has been established
    And ProducerStub and ConsumerStub are in the system
    And The first EventRequestedStub
    When The first EventRequestedStub, with the message "First" is being published
    Then The first EventRequestedStub has been published with the message "First" by the PublisherStub
    Given The second EventRequestedStub
    Then The second EventRequestedStub, has been published with the message "second" by the PublisherStub
    When The ConsumerStub is finished modifying the messages and sends them back into the queue
    Then Each ProducerStub receives back a "World!" message via the queue with their that relate back to their original events
