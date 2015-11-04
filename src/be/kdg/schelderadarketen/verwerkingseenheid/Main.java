package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.StringInputListener;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.SaveToDatabaseStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.RabbitQueue;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.SystemOutRepoImpl;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] argv) throws IOException, InterruptedException, TimeoutException {
        Repository<PositionMessage, Integer> repo = new SystemOutRepoImpl<>();
        StringInputListener<PositionMessage> inputHandler = new StringInputListener<>(PositionMessage.class);
        inputHandler.setQueue(new RabbitQueue<>("localhost", "ping", inputHandler));
        DataProcessingStrategy<PositionMessage> dataProcessingStrategy = new SaveToDatabaseStrategy<>(repo);
        inputHandler.setStrategy(dataProcessingStrategy);
    }

    public static <T> T parseMessage(Class<T> c, String message) {
        try {
            return c.cast(Unmarshaller.unmarshal(c, new StringReader(message)));
        } catch (MarshalException | ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }
}