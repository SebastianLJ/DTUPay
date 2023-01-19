package messageUtilities;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.cqrs.events.Event;
import messageUtilities.cqrs.events.Event2;
import messageUtilities.queues.IDTUPayMessageQueue2;
import messageUtilities.stubs.ConsumerStub;
import messageUtilities.stubs.EventCreatedStub;
import messageUtilities.stubs.EventRequestedStub;
import messageUtilities.stubs.ProducerStub;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Autor Jákup Viljam Dam - s185095
 */
public class MessageQueueSteps {

    private IDTUPayMessageQueue2 messageQueue;
    private ProducerStub producer;
    private ConsumerStub consumer;
    private EventRequestedStub requestedEvent1, requestedEvent2;
    private EventCreatedStub createdEvent1, createdEvent2;
    private ConcurrentHashMap<CorrelationID, CompletableFuture<Event>> requestedEventFutures = new ConcurrentHashMap<>();
    private ConcurrentHashMap<CorrelationID, CompletableFuture<Event>> createdEventFutures = new ConcurrentHashMap<>();

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Before
    public void initialize() {
        requestedEventFutures = new ConcurrentHashMap<>();
        createdEventFutures = new ConcurrentHashMap<>();
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Given("A message queue has been initialized")
    public void aMessageQueueHasBeenInitialized() {
        messageQueue = new IDTUPayMessageQueue2() {
            @Override
            public void publish(Event2 message) {
                switch (message.getType()) {
                    case "EventRequestedStub":
                        EventRequestedStub requestedEvent = message.getArgument(0, EventRequestedStub.class);
                        consumer.consumeQueueEvent(requestedEvent);
                        requestedEventFutures.get(requestedEvent.getCorrelationID()).complete(requestedEvent);
                        break;
                    case "EventCreatedStub":
                        EventCreatedStub createdEvent = message.getArgument(0, EventCreatedStub.class);
                        producer.produceQueueEvent(createdEvent);
                        createdEventFutures.get(createdEvent.getCorrelationID()).complete(createdEvent);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void addHandler(String eventType, Consumer<Event2> handler) {

            }
        };
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @And("A Producer and Consumer have been initialized")
    public void aProducerAndConsumerHaveBeenInitialized() {
        this.producer = new ProducerStub(messageQueue);
        this.consumer = new ConsumerStub(messageQueue);
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @When("A Producer sends a request to the consumer")
    public void aProducerSendsARequestToTheConsumer() {
        this.requestedEvent1 = new EventRequestedStub(CorrelationID.randomID());
        requestedEventFutures.put(requestedEvent1.getCorrelationID(), new CompletableFuture<>());
        createdEventFutures.put(requestedEvent1.getCorrelationID(), new CompletableFuture<>());
        new Thread(() -> {
           createdEvent1 = this.producer.produceEvent(requestedEvent1);
           createdEventFutures.get(createdEvent1.getCorrelationID()).complete(createdEvent1);
        }).start();
    }
    /**
     * @Autor Jákup Viljam Dam - s185095
     */

    @Then("The consumer consumes the request")
    public void theConsumerConsumesTheRequest() {
        EventRequestedStub queuedRequestedEvent = (EventRequestedStub) requestedEventFutures.get(requestedEvent1.getCorrelationID()).join();
        requestedEventFutures.remove(requestedEvent1.getCorrelationID());
        assertEquals(requestedEvent1.getCorrelationID(), queuedRequestedEvent.getCorrelationID());
        assertEquals(requestedEvent1.getMessage(), queuedRequestedEvent.getMessage());
    }

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    @Then("The Producer receives a message back from the consumer")
    public void theProducerReceivesAMessageBackFromTheConsumer() {
        EventCreatedStub queuedCreatedEvent = (EventCreatedStub) createdEventFutures.get(createdEvent1.getCorrelationID()).join();
        createdEventFutures.remove(createdEvent1.getCorrelationID());
        assertEquals(createdEvent1.getCorrelationID(), queuedCreatedEvent.getCorrelationID());
        assertEquals(createdEvent1.getMessage(), queuedCreatedEvent.getMessage());
    }
}
