package be.kdg.schelderadarketen.verwerkingseenheid.domain.wrappers;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;

public class PositionMessageWrapper extends TimeWrapper<PositionMessage, Integer> {

    private PositionMessage positionMessage;
    private int shipId;

    public PositionMessageWrapper(PositionMessage positionMessage, long time) {
        super(positionMessage.getShipId(), positionMessage, time);
        this.shipId = positionMessage.getShipId();
        this.positionMessage = positionMessage;
    }

    public PositionMessage getPositionMessage() {
        return positionMessage;
    }

    @Override
    public String toString() {
        return positionMessage.toString() + " deltaTime: " + getDeltaTime();
    }
}