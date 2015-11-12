package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.shipservice;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.models.ShipInformation;

import java.io.IOException;

public interface ShipInformationService {

    ShipInformation getShipInformation(int shipId) throws IOException, UnknownShipIdException;
}
