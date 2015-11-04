package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.Main;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;

public class StringInputListener<T> implements InputListener<String, T> {

    private Pollable<String> queue;
    private DataProcessingStrategy<T> strategy;

    private Class<T> c;

    public StringInputListener(Class<T> c) {
        this.c = c;
    }

    @Override
    public void setQueue(Pollable<String> queue) {
        this.queue = queue;
    }

    @Override
    public void setStrategy(DataProcessingStrategy<T> strategy) {
        this.strategy = strategy;
    }

    @Override
    public void inputReceived() {
        String data = queue.poll();
        T t = Main.parseMessage(c, data);
        strategy.processData(t);
    }
}