package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.etageneration;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;

import java.util.HashMap;
import java.util.Map;

public class EtaGenerationStrategy implements DataProcessingStrategy {

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
        PositionMessage newPositionMessage = MarshallUtil.parseMessage(PositionMessage.class, data);
        int shipId = newPositionMessage.getShipId();
        PositionMessage previousPositionMessage = ships.get(shipId);

        if (ships.containsKey(shipId)) {
            if (ships.get(shipId) != null) {
                String output = state.processData(previousPositionMessage, newPositionMessage);
                if (output != null)
                    System.out.println("ETA for ship " + shipId + ": " + output);
            }
            ships.put(shipId, newPositionMessage);
        }
    }
}
