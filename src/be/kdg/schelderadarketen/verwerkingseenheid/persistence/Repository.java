package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

import java.util.List;

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
     *
     * @return returns all values
     */
    public List<V> readAll();

    /**
     * @param object object to update
     */
    public void update(V object);

    /**
     * @param key the key at which the object to be removed is stored
     */
    public void delete(K key);

    /**
     * @param field to distinguish what field to check
     */
    public List<V> readAllBy(String field, Object fieldValue);
}
