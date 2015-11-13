package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.etageneration;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class EtaGenerationStrategy implements DataProcessingStrategy {

    private final Logger logger = LogManager.getLogger(EtaGenerationStrategy.class);
    private Map<Integer, PositionMessage> ships;
    private EtaGenerationState state;

    public EtaGenerationStrategy(int[] shipIds, EtaGenerationState state) {
        ships = new HashMap<>();
        this.state = state;
        for (int i = 0; i < shipIds.length; i++)
            ships.put(i, null);
    }

    public void setState(EtaGenerationState state) {
        this.state = state;
    }

    @Override
    public void processData(String data) {
        if (data.contains("<incident>")) return;

        PositionMessage newPositionMessage = MarshallUtil.unmarshall(PositionMessage.class, data);
        int shipId = newPositionMessage.getShipId();
        PositionMessage previousPositionMessage = ships.get(shipId);

        if (ships.containsKey(shipId)) {
            if (ships.get(shipId) != null) {
                String output = state.processData(previousPositionMessage, newPositionMessage);
                if (output != null)
                    logger.info("ETA for ship " + shipId + ": " + output);
            }
            ships.put(shipId, newPositionMessage);
        }
    }
}
