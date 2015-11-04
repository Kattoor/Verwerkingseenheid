package be.kdg.schelderadarketen.verwerkingseenheid.engine.input.rabbitmq;

public interface Pollable<T> {

    T poll();
}