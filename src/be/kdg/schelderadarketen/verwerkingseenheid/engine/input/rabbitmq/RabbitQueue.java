package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.InputHandler;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

/*
* This class acts like a layer between the application and the RabbitMQ service.
* It polls the RabbitMQ service for new messages and adds them to the messages queue.
* When a new message is added to the messages queue, the listeners get notified.
* */
public class RabbitQueue<T> implements Pollable<String> {

    private Queue<String> messages;
    private InputHandler<String> listener;

    public RabbitQueue(String host, String queueName, InputHandler<String> listener) throws IOException, TimeoutException {
        messages = new LinkedList<>();
        this.listener = listener;
        init(host, queueName);
    }

    private void init(String host, String queueName) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                messages.add(message);
                listener.inputReceived();
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    @Override
    public String poll() {
        return messages.poll();
    }
}
