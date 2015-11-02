package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

public interface Repository<V, K> {

    /**
     * @param object object
     * @return returns object
     */
    public V create(V object);

    /**
     * @param key the key at which the desired value is stored
     * @return returns the value
     */
    public V read(K key);

    /**
     * @param object object to update
     */
    public void update(V object);

    /**
     * @param key the key at which the object to be removed is stored
     */
    public void delete(K key);
}
