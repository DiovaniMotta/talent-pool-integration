package br.com.diovani.debezium.spring.boot.handler;

import br.com.diovani.debezium.spring.boot.config.HandlerEvent;
import br.com.diovani.debezium.spring.boot.config.queue.Publisher;
import br.com.diovani.debezium.spring.boot.utils.JSONUtils;
import io.debezium.engine.RecordChangeEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileAssessmentsHandlerEvent implements HandlerEvent<SourceRecord> {

    Publisher<RecordChangeEvent<SourceRecord>> databaseRecordPublisher;

    @Override
    public void execute(RecordChangeEvent<SourceRecord> event) {
        var sourceRecord = event.record();
        databaseRecordPublisher.publish(event);
        log.info("Event published. Database = {}, Key = {}, Value = {}", "profile-assessments", JSONUtils.toJSON(sourceRecord.key()), JSONUtils.toJSON(sourceRecord.value()));
    }
}
