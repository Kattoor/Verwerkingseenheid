package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.InputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.StringInputHandler;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.IncidentActionStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.PositionMessageMemoryBufferStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.SaveToDatabaseStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.etageneration.EtaGenerationEachPositionMessageState;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.etageneration.EtaGenerationStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.QueueApi;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.RabbitQueue;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.wrappers.PositionMessageWrapper;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationService;
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
        QueueApi<String> queueApi = new RabbitQueue("localhost", "ping", inputHandler);
        inputHandler.setQueueApi(queueApi);
        ShipInformationService shipInformationService = new ShipInformationServiceImpl(10 * 60 * 1000, 4);
        DataProcessingStrategy saveToDatabaseStrategy = new SaveToDatabaseStrategy<>(repository, PositionMessage.class);
        DataProcessingStrategy bufferStrategy = new PositionMessageMemoryBufferStrategy(shipInformationService, memoryBuffer, 5000);
        DataProcessingStrategy etaGenerationStrategy = new EtaGenerationStrategy(new int[] {1, 2, 3, 4}, new EtaGenerationEachPositionMessageState());
        DataProcessingStrategy incidentActionStrategy = new IncidentActionStrategy(queueApi, shipInformationService, repository);
        inputHandler.addStrategy(etaGenerationStrategy);
        inputHandler.addStrategy(bufferStrategy);
        inputHandler.addStrategy(saveToDatabaseStrategy);
        inputHandler.addStrategy(incidentActionStrategy);
        queueApi.initialize();
    }

    public static void main(String[] argv) throws IOException, TimeoutException {
        new TestEngine();
    }
}