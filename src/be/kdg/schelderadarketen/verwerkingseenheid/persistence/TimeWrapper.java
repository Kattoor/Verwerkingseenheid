package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PersistenceId;

public abstract class TimeWrapper<T> {

    @PersistenceId
    private T id;
    private long time;

    public TimeWrapper(T id, long time) {
        this.id = id;
        this.time = time;
    }

    public T getId() {
        return id;
    }

    public long getDeltaTime() {
        return time - System.currentTimeMillis();
    }
}