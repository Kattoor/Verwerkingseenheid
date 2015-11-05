package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;

public class StringInputHandler implements InputHandler<String> {

    private Pollable<String> queue;
    @Override
    public void setQueue(Pollable<String> queue) {
        this.queue = queue;
    }
    
    @Override
    public void addStrategy(DataProcessingStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public void inputReceived() {
        String data = queue.poll();
        strategies.forEach(s -> s.processData(data));
    }
}