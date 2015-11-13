package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.Incident;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.IncidentActionReport;
import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.QueueApi;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationService;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.UnknownShipIdException;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;

import java.io.IOException;

public class IncidentActionStrategy implements DataProcessingStrategy {

    private QueueApi<String> queueApi;
    private ShipInformationService shipInformationService;

    public IncidentActionStrategy(QueueApi<String> queueApi, ShipInformationService shipInformationService) {
        this.queueApi = queueApi;
        this.shipInformationService = shipInformationService;
    }

    @Override
    public void processData(String data) {
        if (!data.contains("<incident>")) return;

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
            } else if (incident.getIncidentType().equalsIgnoreCase("man overboord")) {
                report.setAction("AlleSchepenVoorAnker");
            }

            String jsonReport = MarshallUtil.marshall(report);
            queueApi.offer(jsonReport);
            //todo: start controle
        } catch (IOException | UnknownShipIdException e) {
            e.printStackTrace();
        }
    }
}
