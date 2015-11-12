package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationService;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationServiceImpl;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.UnknownShipIdException;

import java.io.IOException;

public class ShipServiceTest {

    public static void main(String[] args) throws IOException {
        try {
            ShipInformationService service = new ShipInformationServiceImpl(10 * 60 * 1000);
            ShipInformation info = service.getShipInformation(3333333);
            System.out.println(info.getIMO());
            System.out.println(info.getDangereousCargo());
            System.out.println(info.getNumberOfPassangers());
            for (int i = 0; i < info.getCargo().length; i++) {
                System.out.println(info.getCargo()[i].getType());
                System.out.println(info.getCargo()[i].getAmount());
            }
        } catch (UnknownShipIdException e) {
            e.printStackTrace();
        }
    }

}
