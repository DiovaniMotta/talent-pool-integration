package br.com.diovani.debezium.spring.boot.config;

import software.amazon.awssdk.regions.Region;

public interface QueueConfig extends SecurityConfig{

    String getUrl();

    Region getRegion();

}
