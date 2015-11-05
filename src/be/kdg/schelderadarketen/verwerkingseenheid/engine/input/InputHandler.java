package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;

/**
 * Polls data from a queue and processes it through a given DataProcessingStrategy
 * @param <T1> Type to be polled from the queue
 * //@param <T2> Type of data to be handled in the DataProcessingStrategy
 */
public interface InputHandler<T1> {

    void inputReceived();
    void setQueue(Pollable<T1> queue);
    void setStrategy(DataProcessingStrategy strategy);
}