package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.utils.MarshallUtil;
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
        if (data.contains("<incident>")) return;
        V v = MarshallUtil.unmarshall(cls, data);
        repo.create(v);
    }
}