package be.kdg.schelderadarketen.verwerkingseenheid.engine;

public class IncidentOccurance {

    private LockDown lockDown;
    private String zone;
    private String type;

    public IncidentOccurance(LockDown lockDown, String zone, String type) {
        this.lockDown = lockDown;
        this.zone = zone;
        this.type = type;
    }

    public LockDown getLockDown() {
        return lockDown;
    }

    public void setLockDown(LockDown lockDown) {
        this.lockDown = lockDown;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
