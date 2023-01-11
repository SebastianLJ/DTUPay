package messageUtilities.queues.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import messageUtilities.queues.IDTUPayMessage;
import messageUtilities.queues.IDTUPayMessageQueue;

import java.io.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class DTUPayRabbitMQ implements IDTUPayMessageQueue {

    private static final String DEFAULT_HOSTNAME = "DTUPayMessageQueue";
    private static final String EXCHANGE_NAME = "eventsExchange";
    private final QueueType queueType;
    private final Channel channel;

    public DTUPayRabbitMQ(QueueType queueType) {
        this.queueType = queueType;
        this.channel = setUpChannel();
    }

    @Override
    public void publish(IDTUPayMessage event) {
        try {
            byte[] data;

            try (ByteArrayOutputStream file = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(file);) {

                out.writeObject(event);
                data = file.toByteArray();
            }
            channel.basicPublish(EXCHANGE_NAME, queueType.name(), null, data);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    public void addHandler(Class<? extends IDTUPayMessage> event, Consumer<IDTUPayMessage> handler) {
        Channel channel = setUpChannel();
        try {
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, queueType.toString());

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                IDTUPayMessage message = null;
                try (ByteArrayInputStream file = new ByteArrayInputStream(delivery.getBody());
                     ObjectInputStream in = new ObjectInputStream(file);) {

                    try {
                        message = (IDTUPayMessage) in.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new Error(e);
                    }
                }
                if (event.equals(message.getClass())) {
                    handler.accept(message);
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
