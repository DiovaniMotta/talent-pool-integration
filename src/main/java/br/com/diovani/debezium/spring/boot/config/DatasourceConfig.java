package br.com.diovani.debezium.spring.boot.config;

import java.util.List;
import java.util.stream.Collectors;

public interface DatasourceConfig {

    String getHost();
    String getUserName();
    String getPassword();
    String getDatabase();
    int getPort();
    List<String>  getWatchTableNames();

    default String watchTablesNames(){
        return getWatchTableNames().stream().collect(Collectors.joining(","));
    }
}
