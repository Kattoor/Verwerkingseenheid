package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

public interface Repository<ObjectType, KeyType> {

    /**
     * @param object object
     * @return returns object
     */
    public ObjectType create(ObjectType object);

    /**
     * @param key the key at which the desired value is stored
     * @return returns the value
     */
    public ObjectType read(KeyType key);

    /**
     * @param object object to update
     */
    public void update(ObjectType object);

    /**
     * @param key the key at which the object to be removed is stored
     */
    public void delete(KeyType key);
}
