package be.kdg.schelderaderketen.verwerkingseenheid;

import java.io.Serializable;
import java.util.Date;

public class PositionMessage implements Serializable {

    private int shipId;
    private int centerId;
    private Date timestamp;
    private int distanceToDock;

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getDistanceToDock() {
        return distanceToDock;
    }

    public void setDistanceToDock(int distanceToDock) {
        this.distanceToDock = distanceToDock;
    }

    @Override
    public String toString() {
        return String.format("ShipId: %s, CenterId: %s, DistanceToDock: %s, Timestamp: %s", getShipId(), getCenterId(), getDistanceToDock(), getTimestamp());
    }
}
