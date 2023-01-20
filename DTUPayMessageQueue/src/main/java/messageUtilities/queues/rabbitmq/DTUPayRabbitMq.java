package messageUtilities.queues.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import messageUtilities.MessageEvent;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @Autor Jákup Viljam Dam - s185095
 * Used the messageUtilities from the course
 */
public class DTUPayRabbitMq implements IDTUPayMessageQueue {

    private static final String TAG = "Message Queue";
    private static final String TOPIC = "events";
    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final String EXCHANGE_NAME = "eventsExchange";
    private static final String QUEUE_TYPE = "topic";

    private Channel channel;
    private String hostname;

    public DTUPayRabbitMq() {
        this(DEFAULT_HOSTNAME);
    }

    public DTUPayRabbitMq(String hostname) {
        this.hostname = hostname;
        channel = setUpChannel();
    }

    @Override
    public void publish(MessageEvent event) {
        String message = new Gson().toJson(event);
        System.out.println(TAG + "::" + message + "::Published");
        try {
            channel.basicPublish(EXCHANGE_NAME, TOPIC, null, message.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private Channel setUpChannel() {
        Channel chan;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(hostname);
            Connection connection = factory.newConnection();
            chan = connection.createChannel();
            chan.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
        } catch (IOException | TimeoutException e) {
            throw new Error(e);
        }
        return chan;
    }

    @Override
    public void addHandler(String eventType, Consumer<MessageEvent> handler) {
        var chan = setUpChannel();
        System.out.println(TAG + "::" + handler.getClass().getSimpleName() + "::" + eventType + "::Listener");
        try {
            String queueName = chan.queueDeclare().getQueue();
            chan.queueBind(queueName, EXCHANGE_NAME, TOPIC);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                MessageEvent event = new Gson().fromJson(message, MessageEvent.class);

                if (eventType.equals(event.getType())) {
                    System.out.println(TAG + "::" + handler.getClass().getName() + "::" + message + "::Consumed");
                    handler.accept(event);
                }
            };
            chan.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e1) {
            throw new Error(e1);
        }
    }
}
