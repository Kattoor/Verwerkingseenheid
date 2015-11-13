package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.etageneration;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;

public class EtaGenerationOnNewZoneState extends EtaGenerationState {

    @Override
    public String processData(PositionMessage previousPositionMessage, PositionMessage newPositionMessage) {
        if (!newPositionMessage.getCenterId().equals(previousPositionMessage.getCenterId()))
            return String.valueOf(calculateEta(previousPositionMessage, newPositionMessage));
        return null;
    }
}
