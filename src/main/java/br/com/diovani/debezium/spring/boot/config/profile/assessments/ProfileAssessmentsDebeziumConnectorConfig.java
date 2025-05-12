package br.com.diovani.debezium.spring.boot.config.profile.assessments;

import br.com.diovani.debezium.spring.boot.config.DatasourceConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileAssessmentsDebeziumConnectorConfig {

    @Autowired
    DatasourceConfig profileAssessmentsDatasourceConfig;

    @Bean(name = "debezium-profile-assessments")
    public io.debezium.config.Configuration create() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        var storageTempFile = new File(tempDir, "debezium_profile_assessments_offsets.dat");
        if (!storageTempFile.exists()) {
            storageTempFile.createNewFile();
        }
        return io.debezium.config.Configuration.create()
                .with("name", "debezium_profile_assessments_connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", storageTempFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")
                .with("database.hostname", profileAssessmentsDatasourceConfig.getHost())
                .with("database.port", profileAssessmentsDatasourceConfig.getPort())
                .with("database.user", profileAssessmentsDatasourceConfig.getUserName())
                .with("database.password", profileAssessmentsDatasourceConfig.getPassword())
                .with("database.dbname", profileAssessmentsDatasourceConfig.getDatabase())
                .with("database.server.id", "10181")
                .with("database.server.name", "customer-postgres-db-server")
                .with("topic.prefix", "debezium-profile-assessments")
                .with("database.history", "io.debezium.relational.history.MemoryDatabaseHistory")
                .with("table.include.list", profileAssessmentsDatasourceConfig.watchTablesNames())
                .with("plugin.name", "pgoutput")
                .with("slot.name", "dbz_profile_assessments_listener")
                .build();
    }

}
