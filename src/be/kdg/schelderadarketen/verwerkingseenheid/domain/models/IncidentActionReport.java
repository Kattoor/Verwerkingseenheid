package be.kdg.schelderadarketen.verwerkingseenheid.domain.models;

public class IncidentActionReport {

    private String imo;
    private String incidentType;
    private int numberOfPassengers;
    private boolean dangerousCargo;
    private String action;

    public String getImo() {
        return imo;
    }

    public void setImo(String imo) {
        this.imo = imo;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public boolean isDangerousCargo() {
        return dangerousCargo;
    }

    public void setDangerousCargo(boolean dangerousCargo) {
        this.dangerousCargo = dangerousCargo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
