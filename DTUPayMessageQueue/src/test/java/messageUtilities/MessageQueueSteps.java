package messageUtilities;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.QueueType;
import messageUtilities.queues.rabbitmq.DTUPayRabbitMQ;
import messageUtilities.stubs.ConsumerStub;
import messageUtilities.stubs.EventCreatedStub;
import messageUtilities.stubs.EventRequestedStub;
import messageUtilities.stubs.ProducerStub;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class MessageQueueSteps {

    private IDTUPayMessageQueue messageQueue;
    private ProducerStub producer;
    private ConsumerStub consumer;
    private final CompletableFuture<EventCreatedStub> simpleRabbitMQSentFuture = new CompletableFuture<>();
    private final CompletableFuture<EventRequestedStub> simpleRabbitMQReceivedFuture = new CompletableFuture<>();

    @Given("DTUPayMessageQueue has been established")
    public void dtupaymessagequeueHasBeenEstablished() {
        this.messageQueue = new DTUPayRabbitMQ(QueueType.DTUPay_TokenManagement) {
            @Override
            public void publish(IDTUPayMessage message) {
                if (message instanceof EventRequestedStub) {
                    simpleRabbitMQReceivedFuture.complete((EventRequestedStub) message);
                }
                super.publish(message);
            }
        };
    }

    @And("ProducerStub and ConsumerStub are in the system")
    public void producerstubAndConsumerStubAreInTheSystem() {
        this.producer = new ProducerStub(this.messageQueue);
        this.consumer = new ConsumerStub(this.messageQueue);
    }

    @When("The ProducerStub sends a {string} message via the queue")
    public void theProducerStubSendsAMessageViaTheQueue(String arg0) {
        EventRequestedStub eventRequestedStub = new EventRequestedStub();
        eventRequestedStub.message = arg0;
        new Thread(() -> {
            EventCreatedStub eventCreated = producer.produceEvent(eventRequestedStub);
            simpleRabbitMQSentFuture.complete(eventCreated);
        }).start();
    }

    @Then("The ConsumerStub receives a {string} message via the queue")
    public void theConsumerStubReceivesAMessageViaTheQueue(String arg0) {
        EventRequestedStub eventRequestedStub = simpleRabbitMQReceivedFuture.join();
        assertNotNull(eventRequestedStub);
        assertEquals(arg0, eventRequestedStub.message);
    }

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

    @Then("The ProducerStub receives back a {string} message via the queue")
    public void theProducerStubReceivesBackAMessageViaTheQueue(String arg0) {
        EventCreatedStub eventCreatedStub = simpleRabbitMQSentFuture.join();
        assertNotNull(eventCreatedStub);
        assertEquals(arg0, eventCreatedStub.message);
    }
}
