package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessingstrategies.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.QueueApi;

public class StringInputHandler implements InputHandler<String> {

    private QueueApi<String> queueApi;
    @Override
    public void setQueueApi(QueueApi<String> queueApi) {
        this.queueApi = queueApi;
    }
    
    @Override
    public void addStrategy(DataProcessingStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public void inputReceived() {
        String data = queueApi.poll();
        strategies.forEach(s -> s.processData(data));
    }
}