package be.kdg.schelderadarketen.verwerkingseenheid.engine;

import java.util.HashMap;
import java.util.Map;

public class Harbor {

    private static Harbor instance;

    private Map<Integer, IncidentOccurance> incidents;

    public static synchronized Harbor getInstance() {
        return instance == null ? instance = new Harbor() : instance;
    }

    private Harbor() {
        incidents = new HashMap<>();
    }

    public Map<Integer, IncidentOccurance> getIncidents() {
        return incidents;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
