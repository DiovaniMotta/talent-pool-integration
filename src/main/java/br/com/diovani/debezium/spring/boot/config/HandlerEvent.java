package br.com.diovani.debezium.spring.boot.config;

import io.debezium.engine.RecordChangeEvent;

public interface HandlerEvent<T> {

    void execute(RecordChangeEvent<T> event);
}
