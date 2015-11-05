package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PositionMessage;

public class PositionMessageWrapper extends TimeWrapper<Integer> {

    private PositionMessage positionMessage;

    public PositionMessageWrapper(PositionMessage positionMessage, long time) {
        super(positionMessage.getShipId(), time);
        this.positionMessage = positionMessage;
    }

    public PositionMessage getPositionMessage() {
        return positionMessage;
    }
}
