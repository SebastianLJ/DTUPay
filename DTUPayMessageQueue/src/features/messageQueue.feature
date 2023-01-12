Feature: Message Queue
  Scenario: A queue is established with messages
    When A queue is created
    And A Consumer and Producer have been created
    Then A EventRequestedStub is sent by the Producer with the message "requestEvent"
    Then The Consumer receives the event and creates a EventCreatedEvent from it and changes the message to "createdEvent"
    Then Finally the producer receives the event back with the new message and event