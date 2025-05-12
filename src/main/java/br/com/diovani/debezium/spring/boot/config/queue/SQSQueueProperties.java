package br.com.diovani.debezium.spring.boot.config.queue;

import br.com.diovani.debezium.spring.boot.config.QueueConfig;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SQSQueueProperties implements QueueConfig {

    @Value("${queue.sqs.region}")
    String region;
    @Value("${queue.sqs.url}")
    String url;
    @Value("${queue.sqs.accessKey}")
    String accessKey;
    @Value("${queue.sqs.secretAccessKey}")
    String secretKey;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Region getRegion() {
        return Region.of(region);
    }

    @Override
    public String getAccessKey() {
        return accessKey;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }
}
