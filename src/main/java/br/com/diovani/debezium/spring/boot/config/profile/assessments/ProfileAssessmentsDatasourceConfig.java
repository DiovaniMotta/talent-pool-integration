package br.com.diovani.debezium.spring.boot.config.profile.assessments;

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
public class ProfileAssessmentsDatasourceConfig implements DatasourceConfig {

    @Value("${profile-assessments.datasource.host}")
    String host;

    @Value("${profile-assessments.datasource.username}")
    String username;

    @Value("${profile-assessments.datasource.password}")
    String password;

    @Value("${profile-assessments.datasource.database}")
    String database;

    @Value("${profile-assessments.datasource.port}")
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
        return List.of();
    }
}
