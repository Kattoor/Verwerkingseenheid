package be.kdg.schelderaderketen.verwerkingseenheid;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.*;
import java.sql.Date;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            Writer writer = new FileWriter("positionmessage.xml");
            Marshaller.marshal(createMessage(), writer);
            writer.close();

            Reader reader = new FileReader("positionmessage.xml");
            PositionMessage positionMessage = (PositionMessage) Unmarshaller.unmarshal(PositionMessage.class, reader);
            System.out.println(positionMessage);
        } catch (IOException | ValidationException | MarshalException e) {
            e.printStackTrace();
        }
    }

    private static PositionMessage createMessage() {
        PositionMessage message = new PositionMessage();
        message.setCenterId(4444);
        message.setDistanceToDock(100);
        message.setShipId(1234567);
        message.setTimestamp(Date.from(Instant.now()));
        return message;
    }
}