package messageUtilities.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.NonNull;
import messageUtilities.cqrs.events.Event;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;
import messageUtilities.queues.QueueType;

import java.io.*;
import java.util.function.Consumer;

/**
 * @Autor Jákup Viljam Dam - s185095
 * Used the mwssageUtilities with some slight modifications, mainly the enums and printouts &
 * restructuring the code a bit
 */
public class DTUPayRabbitMQ implements IDTUPayMessageQueue {

    /**
     * WHEN DEPLOYING TO DOCKER, RABBITMQ SERVICE NAME IS REQUIRED FOR HOSTNAME
     * WHEN TESTING OUTSIDE, USE LOCALHOST AS HOSTNAME
     */
    private final String hostname;
    private final String exchangeName;
    private final String exchangeType;
    private final String queueType;
    private final Channel channel;

    /**
     * @Autor Jákup Viljam Dam - s185095
     */
    public DTUPayRabbitMQ(QueueType queueType, HostnameType hostnameType) {
        this.queueType = queueType.name();
        this.hostname = hostnameType.name();
        this.exchangeName = "eventsExchange";
        this.exchangeType = "topic";
        this.channel = setUpChannel(this.hostname, this.exchangeName, this.exchangeType);
    }

    @Override
    public void publish(IDTUPayMessage message) {
        System.out.println(((Event) message).getCorrelationID());
        try {
            byte[] data;

            try (ByteArrayOutputStream file = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(file);) {

                out.writeObject(message);
                data = file.toByteArray();
            }
            channel.basicPublish(exchangeName, queueType, null, data);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    public void addHandler(Class<? extends IDTUPayMessage> message, Consumer<IDTUPayMessage> handler) {
        Channel channel = setUpChannel(hostname, exchangeName, exchangeType);
        try {
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, queueType);

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
                    System.out.println(((Event) currentMessageInQueue).getCorrelationID());
                    handler.accept(currentMessageInQueue);
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private Channel setUpChannel(@NonNull String hostname, @NonNull String exchangeName, @NonNull String exchangeType) {
        Channel chan;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(hostname);
            Connection connection = factory.newConnection();
            chan = connection.createChannel();
            chan.exchangeDeclare(exchangeName, exchangeType);
        } catch (Exception e) {
            throw new Error(e);
        }
        return chan;
    }
}
