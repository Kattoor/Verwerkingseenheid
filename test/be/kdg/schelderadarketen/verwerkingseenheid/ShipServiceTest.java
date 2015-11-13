package be.kdg.schelderadarketen.verwerkingseenheid;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationService;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.ShipInformationServiceImpl;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice.UnknownShipIdException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ShipServiceTest {

    public static void main(String[] args) throws IOException {
        final Logger logger = LogManager.getLogger(ShipServiceTest.class);

        try {
            ShipInformationService service = new ShipInformationServiceImpl(10 * 60 * 1000, 4);
            ShipInformation info = service.getShipInformation(1234567);
            logger.debug(info.getIMO());
            logger.debug(info.getDangereousCargo());
            logger.debug(info.getNumberOfPassangers());
            for (int i = 0; i < info.getCargo().length; i++) {
                logger.debug(info.getCargo()[i].getType());
                logger.debug(info.getCargo()[i].getAmount());
            }
        } catch (UnknownShipIdException e) {
            e.printStackTrace();
        }
    }

}
