package br.com.diovani.debezium.spring.boot.config.vacancy.management;

import br.com.diovani.debezium.spring.boot.config.HandlerEvent;
import br.com.diovani.debezium.spring.boot.utils.JSONFormater;
import io.debezium.engine.RecordChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VacancyManagementHandlerEvent implements HandlerEvent<SourceRecord> {
    @Override
    public void execute(RecordChangeEvent<SourceRecord> event) {
        var sourceRecord = event.record();
        log.info("Key = {}, Value = {}", JSONFormater.toJSON(sourceRecord.key()), JSONFormater.toJSON(sourceRecord.value()));
    }
}
