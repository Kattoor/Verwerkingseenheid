package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.wrappers.PositionMessageWrapper;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationService;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.UnknownShipIdException;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionMessageMemoryBufferStrategy implements MemoryBufferStrategy {

    private final Logger logger = LogManager.getLogger(PositionMessageMemoryBufferStrategy.class);
    private ShipInformationService shipInformationService;
    private Repository<PositionMessageWrapper, Integer> memoryBuffer;
    private final int MAX_TIME_IN_BUFFER;
    private Map<Integer, ShipInformation> shipInformationMap;

    public PositionMessageMemoryBufferStrategy(ShipInformationService shipInformationService, Repository<PositionMessageWrapper, Integer> memoryBuffer, final int MAX_TIME_IN_BUFFER) {
        this.shipInformationService = shipInformationService;
        this.memoryBuffer = memoryBuffer;
        this.MAX_TIME_IN_BUFFER = MAX_TIME_IN_BUFFER;
        shipInformationMap = new HashMap<>();
        startWorkerThread();
    }

    private void startWorkerThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                    memoryBuffer.readAll().forEach(v -> {
                        if (v.getDeltaTime() >= MAX_TIME_IN_BUFFER) {
                            /* If there is shipInformation stored for this shipId, remove it from the map */
                            int shipId = v.getValue().getShipId();
                            if (shipInformationMap.get(shipId) != null)
                                shipInformationMap.remove(shipId);
                            memoryBuffer.delete(v.getId());
                            logger.debug("Ship with id: " + v.getValue().getShipId() + " deleted!");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void processData(String data) {
        if (data.contains("<incident>")) return;

        /* Map data into object */
        PositionMessage positionMessage = MarshallUtil.unmarshall(PositionMessage.class, data);
        /* Wrap object into it's PositionMessageWrapper object */
        PositionMessageWrapper positionMessageWrapper = new PositionMessageWrapper(positionMessage, System.currentTimeMillis());
        /* Reset timers of ships with same id */
        List<PositionMessageWrapper> messagesWithSameShipId = memoryBuffer.readAllBy("shipId", positionMessageWrapper.getValue().getShipId());
        if (messagesWithSameShipId.size() == 0) {
            int shipId = positionMessage.getShipId();
            try {
                ShipInformation shipInformation = shipInformationService.getShipInformation(shipId);
                shipInformationMap.put(positionMessageWrapper.getValue().getShipId(), shipInformation);
            } catch (IOException e) {
                logger.error("Couldn't connect to ShipInformationService");
            } catch (UnknownShipIdException e) {
                logger.warn("ShipInformationService couldn't find shipId " + shipId);
            }
        } else {
            messagesWithSameShipId.forEach(m -> m.setTime(System.currentTimeMillis()));
        }
        /* Add value to memoryBuffer */
        memoryBuffer.create(positionMessageWrapper);
    }
}