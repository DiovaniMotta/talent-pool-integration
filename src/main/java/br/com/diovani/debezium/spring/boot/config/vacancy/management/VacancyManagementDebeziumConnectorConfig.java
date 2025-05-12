package br.com.diovani.debezium.spring.boot.config.vacancy.management;

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
public class VacancyManagementDebeziumConnectorConfig {

    @Autowired
    DatasourceConfig vacancyManagementDatasourceConfig;

    @Bean(name = "debezium-recruitment-process")
    public io.debezium.config.Configuration create() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        var storageTempFile = new File(tempDir, "debezium_vacancy_management_offsets.dat");
        if (!storageTempFile.exists()) {
            storageTempFile.createNewFile();
        }
        return io.debezium.config.Configuration.create()
                .with("name", "debezium_vacancy_management_connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", storageTempFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")
                .with("database.hostname", vacancyManagementDatasourceConfig.getHost())
                .with("database.port", vacancyManagementDatasourceConfig.getPort())
                .with("database.user", vacancyManagementDatasourceConfig.getUserName())
                .with("database.password", vacancyManagementDatasourceConfig.getPassword())
                .with("database.dbname", vacancyManagementDatasourceConfig.getDatabase())
                .with("database.server.id", "10181")
                .with("database.server.name", "customer-postgres-db-server")
                .with("topic.prefix", "debezium-vacancy-management")
                .with("database.history", "io.debezium.relational.history.MemoryDatabaseHistory")
                .with("table.include.list", vacancyManagementDatasourceConfig.watchTablesNames())
                .with("plugin.name", "pgoutput")
                .with("slot.name", "dbz_vacancy_management_listener")
                .with("snapshot.mode", "initial")
                .with("slot.drop.on.stop", true)
                .build();
    }

}
