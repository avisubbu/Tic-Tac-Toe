package com.rge.tictactoe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@ConfigurationProperties("app")
public class AppProperties {
    private String dataStore;

    public String getDataStore() {
        return dataStore;
    }    

    public void setDataStore(String dataStore) {
        this.dataStore = dataStore;
    }
}
