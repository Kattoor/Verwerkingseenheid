package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;

public class StringInputHandler implements InputHandler<String> {

    private Pollable<String> queue;
    private DataProcessingStrategy strategy;

    @Override
    public void setQueue(Pollable<String> queue) {
        this.queue = queue;
    }
    
    @Override
    public void setStrategy(DataProcessingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void inputReceived() {
        String data = queue.poll();
        strategy.processData(data);
    }
}