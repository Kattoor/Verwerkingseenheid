package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.Incident;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.QueueApi;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.RabbitQueue;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class IncidentFiller {

    public static void main(String[] args) throws IOException, TimeoutException {
        QueueApi<String> queueApi = new RabbitQueue("localhost", "ping", null);
        queueApi.initialize();
        Incident incident = new Incident();
        incident.setImo("IMO1234567");
        incident.setIncidentType("schade");
        String xml = MarshallUtil.marshall(incident);
        RabbitQueue.offerLol(xml);
    }
}
