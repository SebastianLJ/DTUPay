package messageUtilities.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.QueueType;

import java.io.*;
import java.util.function.Consumer;

public class DTUPayRabbitMQ implements IDTUPayMessageQueue {

    private static final String DEFAULT_HOSTNAME = "localhost";
    private static final String EXCHANGE_NAME = "eventsExchange";
    private final QueueType queueType;
    private final Channel channel;

    public DTUPayRabbitMQ(QueueType queueType) {
        this.queueType = queueType;
        this.channel = setUpChannel();
    }

    @Override
    public void publish(IDTUPayMessage message) {
        try {
            byte[] data;

            try (ByteArrayOutputStream file = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(file);) {

                out.writeObject(message);
                data = file.toByteArray();
            }
            channel.basicPublish(EXCHANGE_NAME, queueType.name(), null, data);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    public void addHandler(Class<? extends IDTUPayMessage> message, Consumer<IDTUPayMessage> handler) {
        Channel channel = setUpChannel();
        try {
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, queueType.toString());

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                IDTUPayMessage currentMessageInQueue;
                try (ByteArrayInputStream file = new ByteArrayInputStream(delivery.getBody());
                     ObjectInputStream in = new ObjectInputStream(file);) {

                    try {
                        currentMessageInQueue = (IDTUPayMessage) in.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new Error(e);
                    }
                }
                if (message.equals(currentMessageInQueue.getClass())) {
                    handler.accept(currentMessageInQueue);
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private Channel setUpChannel() {
        Channel chan;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(DEFAULT_HOSTNAME);
            Connection connection = factory.newConnection();
            chan = connection.createChannel();
            chan.exchangeDeclare(EXCHANGE_NAME, "topic");
        } catch (Exception e) {
            throw new Error(e);
        }
        return chan;
    }
}
