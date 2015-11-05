package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

/**
 *
 * @param <V> Type to be stored
 * @param <K> Type used as key
 */
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
