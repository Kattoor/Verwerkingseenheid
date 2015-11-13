package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.etageneration;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;

public abstract class EtaGenerationState {

    public abstract String processData(PositionMessage previousPositionMessage, PositionMessage newPositionMessage);

    public double calculateEta(PositionMessage previousPositionMessage, PositionMessage newPositionMessage) {
        int distanceToDock = newPositionMessage.getDistanceToDock();
        double currentVelocity = (previousPositionMessage.getDistanceToDock() - newPositionMessage.getDistanceToDock()) /
                (newPositionMessage.getTimestamp().getTime() / 1000 - previousPositionMessage.getTimestamp().getTime() / 1000);
        return distanceToDock / currentVelocity;
    }
}
