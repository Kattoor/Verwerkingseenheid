package be.kdg.schelderadarketen.verwerkingseenheid.engine.input;

import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.dataprocessing.DataProcessingStrategy;
import be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq.Pollable;

public interface InputListener<T1, T2> {

    void inputReceived();
    void setQueue(Pollable<T1> queue);
    void setStrategy(DataProcessingStrategy<T2> strategy);
}