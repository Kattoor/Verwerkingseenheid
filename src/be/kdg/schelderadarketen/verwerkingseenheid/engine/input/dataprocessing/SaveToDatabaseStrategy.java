package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing;

import be.kdg.schelderadarketen.verwerkingseenheid.Main;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;

public class SaveToDatabaseStrategy<V> implements DataProcessingStrategy {

    private Repository<V, ?> repo;
    private Class<V> cls;

    public SaveToDatabaseStrategy(Repository<V, ?> repo, Class<V> cls) {
        this.repo = repo;
        this.cls = cls;
    }

    @Override
    public void processData(String data) {
        V v = Main.parseMessage(cls, data);
        repo.create(v);
    }
}