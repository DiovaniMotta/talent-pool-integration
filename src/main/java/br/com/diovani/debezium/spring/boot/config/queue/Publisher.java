package br.com.diovani.debezium.spring.boot.config.queue;

public interface Publisher<T> {

    void publish(T value);
}
