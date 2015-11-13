package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq;

public interface QueueApi<T> {

    void initialize();
    T poll();
    void offer(T t);
}