package be.kdg.schelderadarketen.verwerkingseenheid.domain.models;

import java.io.Serializable;

public class ShipInformation implements Serializable {

    private String IMO;
    private boolean dangereousCargo;
    private int numberOfPassangers;
    private Cargo[] cargo;

    public String getIMO() {
        return IMO;
    }

    @SuppressWarnings("unused")
    public void setIMO(String IMO) {
        this.IMO = IMO;
    }

    public boolean getDangereousCargo() {
        return dangereousCargo;
    }

    @SuppressWarnings("unused")
    public void setDangereousCargo(boolean dangerousCargo) {
        this.dangereousCargo = dangerousCargo;
    }

    public int getNumberOfPassangers() {
        return numberOfPassangers;
    }

    @SuppressWarnings("unused")
    public void setNumberOfPassangers(int numberOfPassangers) {
        this.numberOfPassangers = numberOfPassangers;
    }

    public Cargo[] getCargo() {
        return cargo;
    }

    @SuppressWarnings("unused")
    public void setCargo(Cargo[] cargo) {
        this.cargo = cargo;
    }
}
