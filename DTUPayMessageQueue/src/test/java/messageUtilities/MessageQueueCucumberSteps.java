package messageUtilities;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.events.EventID;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.queues.QueueType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageQueueCucumberSteps {

    private IDTUPayMessageQueue messageQueue;
    private ConsumerStub consumer;
    private ProducerStub producer;
    private EventCreatedStub eventCreated;

    @When("A queue is created")
    public void aQueueIsCreated() {
        this.messageQueue = new DTUPayRabbitMQ(QueueType.Default);
    }

    @And("A Consumer and Producer have been created")
    public void aConsumerAndProducerHaveBeenCreated() {
        this.consumer = new ConsumerStub(this.messageQueue);
        this.producer = new ProducerStub(this.messageQueue);
    }

    @Then("A EventRequestedStub is sent by the Producer with the message {string}")
    public void aEventRequestedStubIsSentByTheProducerWithTheMessage(String requestEvent) {
        EventRequestedStub event = new EventRequestedStub(new EventID(UUID.randomUUID()));
        event.message = requestEvent;
        eventCreated = this.producer.produceEvent(event);
        assertEquals(requestEvent, producer.currentEventRequested.message);
    }

    @Then("The Consumer receives the event and creates a EventCreatedEvent from it and changes the message to {string}")
    public void theConsumerReceivesTheEventAndCreatesAEventCreatedEventFromItAndChangesTheMessageTo(String createdEvent) {
        assertEquals(createdEvent, consumer.currentEventCreated.message);
    }

    @Then("Finally the producer receives the event back with the new message and event")
    public void finallyTheProducerReceivesTheEventBackWithTheNewMessageAndEvent() {
        assertEquals(eventCreated.message, consumer.currentEventCreated.message);
    }
}
