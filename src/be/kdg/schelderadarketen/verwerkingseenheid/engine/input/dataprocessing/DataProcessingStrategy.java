package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing;

public interface DataProcessingStrategy<V> {

    void processData(V v);
}
