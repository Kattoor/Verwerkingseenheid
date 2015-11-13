package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformationError;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.GsonDeserializer;
import be.kdg.se3.proxy.ShipServiceProxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShipInformationServiceImpl implements ShipInformationService {

    private ShipServiceProxy shipServiceProxy;
    private Map<Integer, ShipInformation> cachedShipInformationMap;
    private int durationCachedShipInfo;
    private long previousMapRefresh;
    private int connectionFailureAttempts;
    private int connectionAttempt;

    public ShipInformationServiceImpl(int durationCachedShipInfo, int connectionFailureAttempts) {
        this.durationCachedShipInfo = durationCachedShipInfo;
        previousMapRefresh = System.currentTimeMillis();
        this.shipServiceProxy = new ShipServiceProxy();
        this.connectionFailureAttempts = connectionFailureAttempts;
        cachedShipInformationMap = new HashMap<>();
    }

    private void updateMap() {
        long timeNow = System.currentTimeMillis();
        if (timeNow - previousMapRefresh > durationCachedShipInfo) {
            cachedShipInformationMap.clear();
            previousMapRefresh = timeNow;
        }
    }

    @Override
    public ShipInformation getShipInformation(int shipId) throws IOException, UnknownShipIdException {
        updateMap();

        ShipInformation shipInformation = cachedShipInformationMap.get(shipId);
        if (shipInformation != null) return shipInformation;

        try {
            String response = shipServiceProxy.get("www.services4se3.com/shipservice/" + shipId);
            connectionAttempt = 0;
            if (!response.startsWith("{\"error\":")) {
                shipInformation = GsonDeserializer.deserialize(response, ShipInformation.class);
                cachedShipInformationMap.put(shipId, shipInformation);
                return shipInformation;
            } else {
                ShipInformationError error = GsonDeserializer.deserialize(response, ShipInformationError.class);
                throw new UnknownShipIdException(error.getError() + ": " + error.getDescription());
            }
        } catch (IOException e) {
            if (++connectionAttempt == connectionFailureAttempts) {
                e.printStackTrace();
                System.exit(1);
                return null;
            } else throw new IOException(e);
        }
    }
}