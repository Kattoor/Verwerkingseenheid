package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.InputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.StringInputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.PositionMessageMemoryBufferStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.SaveToDatabaseStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.etageneration.EtaGenerationEachPositionMessageState;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.etageneration.EtaGenerationStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.RabbitQueue;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.wrappers.PositionMessageWrapper;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationServiceImpl;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.memoryDb.SystemOutRepoImpl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestEngine {

    /*
    * Pollable sends it's listener - inputHandler - a notification when new data is pollable
    * InputHandler's strategies get invoked when it receives a notification
    * */
    public TestEngine() throws IOException, TimeoutException {
        Repository<PositionMessage, Integer> repository = new SystemOutRepoImpl<>();
        Repository<PositionMessageWrapper, Integer> memoryBuffer = new SystemOutRepoImpl<>();
        InputHandler<String> inputHandler = new StringInputHandler();
        DataProcessingStrategy saveToDatabaseStrategy = new SaveToDatabaseStrategy<>(repository, PositionMessage.class);
        DataProcessingStrategy bufferStrategy = new PositionMessageMemoryBufferStrategy(
                new ShipInformationServiceImpl(10 * 60 * 1000, 4), memoryBuffer, 5000);
        DataProcessingStrategy etaGenerationStrategy = new EtaGenerationStrategy(new int[] {1, 2, 3, 4}, new EtaGenerationEachPositionMessageState());
        Pollable<String> pollable = new RabbitQueue("localhost", "ping", inputHandler);
        inputHandler.setQueue(pollable);
        inputHandler.addStrategy(etaGenerationStrategy);
        //inputHandler.addStrategy(bufferStrategy);
        //inputHandler.addStrategy(saveToDatabaseStrategy);
        pollable.initialize();
    }

    public static void main(String[] argv) throws IOException, TimeoutException {
        new TestEngine();
    }
}