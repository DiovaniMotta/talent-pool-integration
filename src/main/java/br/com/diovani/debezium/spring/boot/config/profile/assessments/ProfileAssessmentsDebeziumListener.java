package br.com.diovani.debezium.spring.boot.config.profile.assessments;

import br.com.diovani.debezium.spring.boot.config.HandlerEvent;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileAssessmentsDebeziumListener {

    Executor executor = Executors.newWorkStealingPool();
    DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

    public ProfileAssessmentsDebeziumListener(
            @Qualifier("debezium-profile-assessments")
            Configuration configuration,
            @Qualifier("profileAssessmentsHandlerEvent")
            HandlerEvent<SourceRecord> profileAssessmentsHandlerEvent) {
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(configuration.asProperties())
                .notifying(profileAssessmentsHandlerEvent::execute)
                .build();
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (Objects.nonNull(this.debeziumEngine)) {
            this.debeziumEngine.close();
        }
    }
}
