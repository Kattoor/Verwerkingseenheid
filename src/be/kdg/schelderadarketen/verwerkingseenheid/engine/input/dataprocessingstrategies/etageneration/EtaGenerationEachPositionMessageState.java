package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.etageneration;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;

public class EtaGenerationEachPositionMessageState extends EtaGenerationState {

    @Override
    public String processData(PositionMessage previousPositionMessage, PositionMessage newPositionMessage) {
        return String.valueOf(calculateEta(previousPositionMessage, newPositionMessage));
    }
}
