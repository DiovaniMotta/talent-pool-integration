package br.com.diovani.debezium.spring.boot.config.vacancy.management;

import br.com.diovani.debezium.spring.boot.config.DatasourceConfig;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VacancyManagementDatasourceConfig implements DatasourceConfig {

    @Value("${recruitment-process.datasource.host}")
    String host;

    @Value("${recruitment-process.datasource.username}")
    String username;

    @Value("${recruitment-process.datasource.password}")
    String password;

    @Value("${recruitment-process.datasource.database}")
    String database;

    @Value("${recruitment-process.datasource.port}")
    int port;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public List<String> getWatchTableNames() {
        return List.of("candidate", "resume");
    }
}
