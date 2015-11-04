package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing;

import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;

public class SaveToDatabaseStrategy<V> implements DataProcessingStrategy<V> {

    private Repository<V, ?> repo;

    public SaveToDatabaseStrategy(Repository<V, ?> repo) {
        this.repo = repo;
    }

    @Override
    public void processData(V data) {
        repo.create(data);
    }
}