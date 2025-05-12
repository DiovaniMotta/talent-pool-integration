package br.com.diovani.debezium.spring.boot.config.queue;

import com.google.gson.Gson;
import io.debezium.engine.RecordChangeEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQSQueueService implements Publisher<RecordChangeEvent<SourceRecord>> {

    @Autowired
    SQSQueueProperties properties;
    @Autowired
    SqsClient client;

    @Override
    public void publish(RecordChangeEvent<SourceRecord> value) {
        var message = new Gson().toJson(value);
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(properties.getUrl())
                .messageBody(message)
                .build();

        var response = client.sendMessage(request);
        if (!response.sdkHttpResponse().isSuccessful()) {
            throw new IllegalCallerException("Failed to send message to SQS: " + response.sdkHttpResponse().statusText().orElse("Unknown error"));
        }
    }
}
