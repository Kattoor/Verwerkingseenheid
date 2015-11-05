package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;

import java.util.ArrayList;
import java.util.List;

/**
 * Polls data from a queue and processes it through a given DataProcessingStrategy
 * @param <T1> Type to be polled from the queue
 * //@param <T2> Type of data to be handled in the DataProcessingStrategy
 */
public interface InputHandler<T1> {

    List<DataProcessingStrategy> strategies = new ArrayList<>();
    void inputReceived();
    void setQueue(Pollable<T1> queue);
    void addStrategy(DataProcessingStrategy strategy);
}