package be.kdg.schelderadarketen.verwerkingseenheid.domain.models;

public class Incident {

    private String imo;
    private String incidentType;

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

    @Override
    public String toString() {
        return "IMO: " + imo + "\tIncidentType: " + incidentType;
    }
}
