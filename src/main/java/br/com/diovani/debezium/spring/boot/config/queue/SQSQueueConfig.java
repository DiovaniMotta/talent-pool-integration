package br.com.diovani.debezium.spring.boot.config.queue;

import br.com.diovani.debezium.spring.boot.config.QueueConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SQSQueueConfig {

    @Autowired
    QueueConfig queueConfig;

    @Bean
    public SqsClient create(){
        var awsCredentials = AwsBasicCredentials.create(queueConfig.getAccessKey(), queueConfig.getAccessKey());
        return SqsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(queueConfig.getRegion())
                .build();
    }

}
