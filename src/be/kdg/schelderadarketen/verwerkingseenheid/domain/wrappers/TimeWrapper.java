package be.kdg.schelderadarketen.verwerkingseenheid.domain.wrappers;

import be.kdg.schelderadarketen.verwerkingseenheid.persistence.memoryDb.PersistenceId;

public abstract class TimeWrapper<V, K> {

    @PersistenceId
    public K id;
    private V value;
    private long time;

    public TimeWrapper(K id, V value, long time) {
        setId(id);
        setValue(value);
        setTime(time);
    }

    public void setId(K id) {
        this.id = id;
    }

    public K getId() {
        return id;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public long getDeltaTime() {
        return System.currentTimeMillis() - time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}