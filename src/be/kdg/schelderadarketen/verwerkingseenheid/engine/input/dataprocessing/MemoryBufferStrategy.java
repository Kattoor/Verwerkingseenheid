package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing;

import be.kdg.schelderadarketen.verwerkingseenheid.Main;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.TimeWrapper;

public class MemoryBufferStrategy<V extends TimeWrapper, K> implements DataProcessingStrategy {

    private Repository<V, K> memoryBuffer;
    private Class<V> clsV;
    private Class<K> clsK;

    public MemoryBufferStrategy(Repository<V, K> memoryBuffer, Class<V> clsV, Class<K> clsK) {
        this.memoryBuffer = memoryBuffer;
        this.clsV = clsV;
        this.clsK = clsK;
        startWorkerThread();
    }

    private void startWorkerThread() {
        new Thread(() -> {
            memoryBuffer.readAll().forEach(v -> {
                if (clsV.cast(v).getDeltaTime() > 5000) {
                    //todo: 5000 instelbaar maken (constructor?)
                    memoryBuffer.delete(clsK.cast(v.getId()));
                }
            });
        }).start();
    }

    @Override
    public void processData(String data) {
        V v = Main.parseMessage(clsV, data);
        memoryBuffer.create(v);
    }
}