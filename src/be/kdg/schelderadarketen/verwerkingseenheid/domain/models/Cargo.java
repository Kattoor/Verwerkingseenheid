package be.kdg.schelderadarketen.verwerkingseenheid.domain.models;

import java.io.Serializable;

public class Cargo implements Serializable {

    private String type;
    private int amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
