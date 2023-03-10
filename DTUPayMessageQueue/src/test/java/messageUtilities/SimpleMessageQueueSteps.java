package messageUtilities;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.cqrs.CorrelationID;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueueCQRS;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMqCQRS;
import messageUtilities.queues.rabbitmq.HostnameType;
import messageUtilities.stubs.ConsumerStub;
import messageUtilities.stubs.EventCreatedStub;
import messageUtilities.stubs.EventRequestedStub;
import messageUtilities.stubs.ProducerStub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class SimpleMessageQueueSteps {
    private IDTUPayMessageQueueCQRS messageQueue;
    private ProducerStub producer;
    private ConsumerStub consumer;
    private EventRequestedStub requestedEvent1, requestedEvent2;
    private final CompletableFuture<EventCreatedStub> simpleRabbitMQCorrelationSentFuture1 = new CompletableFuture<>();
    private final CompletableFuture<EventCreatedStub> simpleRabbitMQCorrelationSentFuture2 = new CompletableFuture<>();
    private final CompletableFuture<EventCreatedStub> simpleRabbitMQSentFuture = new CompletableFuture<>();
    private final Map<CorrelationID, CompletableFuture<EventRequestedStub>> publishedEvents = new HashMap<>();

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Given("DTUPayMessageQueue has been established")
    public void dtupaymessagequeueHasBeenEstablished() {
        this.messageQueue = new DTUPayRabbitMqCQRS(QueueType.DTUPay, HostnameType.localhost) {
            @Override
            public void publish(IDTUPayMessage message) {
                if (message instanceof EventRequestedStub) {
                    publishedEvents.get(((EventRequestedStub) message).getCorrelationID()).complete((EventRequestedStub) message);
                }
                super.publish(message);
            }
        };
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @And("ProducerStub and ConsumerStub are in the system")
    public void producerstubAndConsumerStubAreInTheSystem() {
        //this.producer = new ProducerStub(this.messageQueue);
        //this.consumer = new ConsumerStub(this.messageQueue);
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @When("The ProducerStub sends a {string} message via the queue")
    public void theProducerStubSendsAMessageViaTheQueue(String arg0) {
        requestedEvent1.setMessage(arg0);
        new Thread(() -> {
            EventCreatedStub eventCreated = producer.produceEvent(requestedEvent1);
            simpleRabbitMQSentFuture.complete(eventCreated);
        }).start();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Then("The ConsumerStub receives a {string} message via the queue")
    public void theConsumerStubReceivesAMessageViaTheQueue(String arg0) {
        EventRequestedStub eventRequestedStub = publishedEvents.get(requestedEvent1.getCorrelationID()).join();
        assertNotNull(eventRequestedStub);
        assertEquals(requestedEvent1.getCorrelationID(), eventRequestedStub.getCorrelationID());
        assertEquals(arg0, eventRequestedStub.getMessage());
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @When("The ConsumerStub is finished modifying the message and sends it back into the queue")
    public void theConsumerStubIsFinishedModifyingTheMessageAndSendsItBackIntoTheQueue() {
        while (!simpleRabbitMQSentFuture.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Then("The ProducerStub receives back a {string} message via the queue")
    public void theProducerStubReceivesBackAMessageViaTheQueue(String arg0) {
        EventCreatedStub eventCreatedStub = simpleRabbitMQSentFuture.join();
        assertNotNull(eventCreatedStub);
        assertEquals(arg0, eventCreatedStub.getMessage());
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @And("The first EventRequestedStub")
    public void theFirstEventRequestedStub() {
        this.requestedEvent1 = new EventRequestedStub(CorrelationID.randomID());
        this.publishedEvents.put(requestedEvent1.getCorrelationID(), new CompletableFuture<>());
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @When("The first EventRequestedStub, with the message {string} is being published")
    public void theFirstEventRequestedStubWithTheMessageIsBeingPublished(String arg0) {
        requestedEvent1.setMessage(arg0);
        new Thread(() -> {
            EventCreatedStub createdEvent1 = producer.produceEvent(requestedEvent1);
            simpleRabbitMQCorrelationSentFuture1.complete(createdEvent1);
        }).start();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Then("The first EventRequestedStub has been published with the message {string} by the PublisherStub")
    public void theFirstEventRequestedStubHasBeenPublishedWithTheMessageByThePublisherStub(String arg0) {
        EventRequestedStub eventRequestedStub = publishedEvents.get(requestedEvent1.getCorrelationID()).join();
        assertNotNull(eventRequestedStub);
        assertEquals(requestedEvent1.getCorrelationID(), eventRequestedStub.getCorrelationID());
        assertEquals(arg0, eventRequestedStub.getMessage());
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Given("The second EventRequestedStub")
    public void theSecondEventRequestedStub() {
        this.requestedEvent2 = new EventRequestedStub(CorrelationID.randomID());
        this.publishedEvents.put(requestedEvent2.getCorrelationID(), new CompletableFuture<>());
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Then("The second EventRequestedStub, has been published with the message {string} by the PublisherStub")
    public void theSecondEventRequestedStubHasBeenPublishedWithTheMessageByThePublisherStub(String arg0) {
        requestedEvent2.setMessage(arg0);
        new Thread(() -> {
            EventCreatedStub createdEvent2 = producer.produceEvent(requestedEvent2);
            simpleRabbitMQCorrelationSentFuture2.complete(createdEvent2);
        }).start();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @When("The ConsumerStub is finished modifying the messages and sends them back into the queue")
    public void theConsumerStubIsFinishedModifyingTheMessagesAndSendsThemBackIntoTheQueue() {
        while (!simpleRabbitMQCorrelationSentFuture2.isDone() && !simpleRabbitMQCorrelationSentFuture1.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Then("Each ProducerStub receives back a {string} message via the queue with their that relate back to their original events")
    public void eachProducerStubReceivesBackAMessageViaTheQueueWithTheirThatRelateBackToTheirOriginalEvents(String arg0) {
        EventCreatedStub createdEvent1 = simpleRabbitMQCorrelationSentFuture1.join();
        EventCreatedStub createdEvent2 = simpleRabbitMQCorrelationSentFuture2.join();

        assertNotNull(createdEvent1);
        assertNotNull(createdEvent2);
        assertEquals(arg0, createdEvent1.getMessage());
        assertEquals(arg0, createdEvent2.getMessage());
        assertEquals(requestedEvent1.getCorrelationID(), createdEvent1.getCorrelationID());
        assertEquals(requestedEvent2.getCorrelationID(), createdEvent2.getCorrelationID());
        assertNotEquals(requestedEvent1.getCorrelationID(), requestedEvent2.getCorrelationID());
        assertNotEquals(createdEvent1.getCorrelationID(), createdEvent2.getCorrelationID());
    }
}
