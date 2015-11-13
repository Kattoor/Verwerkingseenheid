package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.Incident;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.IncidentActionReport;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.PositionMessage;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.Harbor;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.IncidentOccurance;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.LockDown;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.QueueApi;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationService;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.UnknownShipIdException;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class IncidentActionStrategy implements DataProcessingStrategy {

    private QueueApi<String> queueApi;
    private ShipInformationService shipInformationService;
    private int shipIdOfCauser;
    private Harbor harbor;
    private Repository<PositionMessage, Integer> repository;

    public IncidentActionStrategy(QueueApi<String> queueApi, ShipInformationService shipInformationService, Repository<PositionMessage, Integer> repository) {
        this.queueApi = queueApi;
        this.shipInformationService = shipInformationService;
        this.repository = repository;
        harbor = Harbor.getInstance();
    }

    @Override
    public void processData(String data) {
        if (!data.contains("<incident>")) {
            PositionMessage positionMessage = MarshallUtil.unmarshall(PositionMessage.class, data);
            List<PositionMessage> list = repository.readAllBy("shipId", positionMessage.getShipId());
            list.sort(getPositionMessageComparator());
            int previousDistanceToDock = list.get(0).getDistanceToDock();
            String centerId = list.get(0).getCenterId();
            harbor.getIncidents().keySet().forEach(k -> {
                switch (harbor.getIncidents().get(k).getLockDown()) {
                    case ALL:
                        if (positionMessage.getDistanceToDock() != previousDistanceToDock) {
                            sendOffense(positionMessage, harbor.getIncidents().get(k));
                        }
                        break;
                    case ZONE:
                        if (harbor.getIncidents().get(k).getZone().equals(centerId) && positionMessage.getDistanceToDock() != previousDistanceToDock) {
                            sendOffense(positionMessage, harbor.getIncidents().get(k));
                        }
                        break;
                }
            });
        } else if (data.contains("<incident>")) {
            try {
                IncidentActionReport report = new IncidentActionReport();
                Incident incident = MarshallUtil.unmarshall(Incident.class, data);
                String shipImo = incident.getImo();
                int shipId = Integer.parseInt(shipImo.substring(3));
                ShipInformation shipInformation = shipInformationService.getShipInformation(shipId);
                boolean dangerousCargo = shipInformation.getDangereousCargo();
                report.setImo(shipInformation.getIMO());
                report.setDangerousCargo(dangerousCargo);
                report.setNumberOfPassengers(shipInformation.getNumberOfPassangers());
                report.setIncidentType(incident.getIncidentType());

                if (incident.getIncidentType().equalsIgnoreCase("schade")) {
                    report.setAction(dangerousCargo ? "AlleSchepenVoorAnker" : "AlleSchepenInZoneVoorAnker");
                    List<PositionMessage> list = repository.readAllBy("shipId", shipId);
                    list.sort(getPositionMessageComparator());
                    harbor.getIncidents().put(shipId, new IncidentOccurance(dangerousCargo ? LockDown.ALL : LockDown.ZONE, list.get(0).getCenterId(), incident.getIncidentType()));
                } else if (incident.getIncidentType().equalsIgnoreCase("man overboord")) {
                    report.setAction("AlleSchepenVoorAnker");
                    List<PositionMessage> list = repository.readAllBy("shipId", shipId);
                    list.sort(getPositionMessageComparator());
                    harbor.getIncidents().put(shipId, new IncidentOccurance(LockDown.ALL, list.get(0).getCenterId(), incident.getIncidentType()));
                } else if (incident.getIncidentType().equalsIgnoreCase("alles goed")) {
                    if (shipId == shipIdOfCauser) {
                        report.setAction("alles goed");
                        harbor.getIncidents().remove(shipId);
                    }
                }

                shipIdOfCauser = shipId;
                String jsonReport = MarshallUtil.marshall(report);
                queueApi.offer(jsonReport);
            } catch (IOException | UnknownShipIdException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendOffense(PositionMessage positionMessage, IncidentOccurance incident) {
        try {
            IncidentActionReport report = new IncidentActionReport();
            report.setAction("ZwareOvertreding");
            ShipInformation shipInformation = shipInformationService.getShipInformation(positionMessage.getShipId());
            report.setNumberOfPassengers(shipInformation.getNumberOfPassangers());
            report.setDangerousCargo(shipInformation.getDangereousCargo());
            report.setImo(shipInformation.getIMO());
            report.setIncidentType(incident.getType());
        } catch (IOException | UnknownShipIdException e) {
            e.printStackTrace();
        }
    }

    private Comparator<PositionMessage> getPositionMessageComparator() {
        return (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}
