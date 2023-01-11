import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class DTUPayMessageQueue implements IDTUPayMessageQueue {

    private static final String DEFAULT_HOSTNAME = "DTUPayMessageQueue";
    private static final String EXCHANGE_NAME = "eventsExchange";
    private final QueueType queueType;
    private final Channel channel;

    public DTUPayMessageQueue(QueueType queueType) {
        this.queueType = queueType;
        this.channel = setUpChannel();
    }

    @Override
    public void publish(Event event) {
        String message = new Gson().toJson(event);
        System.out.println("[x] Publish event " + message);
        try {
            channel.basicPublish(EXCHANGE_NAME, queueType.toString(), null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Override
    public void addHandler(EventType eventType, Consumer<Event> handler) {
        Channel channel = setUpChannel();
        System.out.println("[x] handler " + handler + " for event type " + eventType + " installed");
        try {
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, queueType.toString());

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                System.out.println("[x] handle event " + message);

                Event event = new Gson().fromJson(message, Event.class);

                if (eventType.equals(event.getEventType())) {
                    handler.accept(event);
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e1) {
            throw new Error(e1);
        }
    }

    private Channel setUpChannel() {
        Channel chan;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(DEFAULT_HOSTNAME);
            Connection connection = factory.newConnection();
            chan = connection.createChannel();
            chan.exchangeDeclare(EXCHANGE_NAME, queueType.toString());
        } catch (IOException | TimeoutException e) {
            throw new Error(e);
        }
        return chan;
    }
}
