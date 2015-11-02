package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PositionMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class QueueFiller {

    private Connection connection;
    private Channel channel;

    public QueueFiller() {
        try {
            init();
            generateMessages(10000).parallelStream().forEach(m -> {
                Writer w = new StringWriter();
                try {
                    Marshaller.marshal(m, w);
                    sendMessage(w.toString());
                } catch (MarshalException | ValidationException | IOException e) {
                    e.printStackTrace();
                }
            });
            finish();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private List<PositionMessage> generateMessages(int amount) {
        List<PositionMessage> messages = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            PositionMessage message = new PositionMessage();
            message.setShipId(i);
            message.setCenterId("Center" + i);
            message.setTimestamp(Date.from(Instant.now()));
            message.setDistanceToDock(new Random().nextInt(1000));
            messages.add(message);
        }
        return messages;
    }

    private void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare("ping", true, false, false, null);
    }

    private void sendMessage(String message) throws IOException {
        channel.basicPublish("", "ping", null, message.getBytes());
    }

    private void finish() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new QueueFiller();
    }
}
