package be.kdg.schelderadarketen.verwerkingseenheid.domain.models;

import be.kdg.schelderadarketen.verwerkingseenheid.persistence.memoryDb.PersistenceId;

import java.io.Serializable;
import java.util.Date;

public class PositionMessage implements Serializable {

    @PersistenceId
    private int id;
    private int shipId;
    private String centerId;
    private Date timestamp;
    private int distanceToDock;

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
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
