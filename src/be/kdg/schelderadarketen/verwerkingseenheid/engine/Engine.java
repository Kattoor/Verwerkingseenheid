package be.kdg.schelderadarketen.verwerkingseenheid.engine;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.InputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.StringInputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.SaveToDatabaseStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.RabbitQueue;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.SystemOutRepoImpl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Engine {

    private Repository<PositionMessage, Integer> repository;
    private Repository<PositionMessage, Integer> buffer;
    private InputHandler<String> inputHandler;
    private DataProcessingStrategy dataProcessingStrategy;

    public Engine() {
        initEngine();


    }

    private void initEngine() {
        try {
            repository = new SystemOutRepoImpl<>();
            /* Due to type erasure we have to pass our PositionMessage class to the constructor of the StringInputListener */
            inputHandler = new StringInputHandler();
            /* We pass our inputHandler to the queue so it knows who to notify when it receives data */
            Pollable<String> pollable = new RabbitQueue<>("localhost", "ping", inputHandler);
            inputHandler.setQueue(pollable);
            dataProcessingStrategy = new SaveToDatabaseStrategy<>(repository, PositionMessage.class);
            /* We inject the desired strategy in the inputListener so it knows what to do when it receives data */
            inputHandler.setStrategy(dataProcessingStrategy);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}