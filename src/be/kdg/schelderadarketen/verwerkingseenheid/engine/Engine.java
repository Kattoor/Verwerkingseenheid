package be.kdg.schelderadarketen.verwerkingseenheid.engine;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.InputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.StringInputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.MemoryBufferStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.SaveToDatabaseStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.RabbitQueue;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.PositionMessageWrapper;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.SystemOutRepoImpl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Engine {

    private Repository<PositionMessage, Integer> repository;
    private Repository<PositionMessageWrapper, Integer> memoryBuffer;
    private InputHandler<String> inputHandler;

    public Engine() {
        initEngine();


    }

    private void initEngine() {
        try {
            /* Create database and memory buffer and assign them to the inputhandler-strategies */
            repository = new SystemOutRepoImpl<>();
            memoryBuffer = new SystemOutRepoImpl<>();
            DataProcessingStrategy saveToDatabaseStrategy = new SaveToDatabaseStrategy<>(repository, PositionMessage.class);
            DataProcessingStrategy bufferStrategy = new MemoryBufferStrategy<>(memoryBuffer, PositionMessageWrapper.class, Integer.class);

            /* Due to type erasure we have to pass our PositionMessage class to the constructor of the StringInputListener */
            inputHandler = new StringInputHandler();

            /* We pass our inputHandler to the queue so it knows who to notify when it receives data */
            Pollable<String> pollable = new RabbitQueue<>("localhost", "ping", inputHandler);
            inputHandler.setQueue(pollable);

            /* We inject the desired strategy in the inputListener so it knows what to do when it receives data */
            inputHandler.addStrategy(saveToDatabaseStrategy);
            inputHandler.addStrategy(bufferStrategy);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}