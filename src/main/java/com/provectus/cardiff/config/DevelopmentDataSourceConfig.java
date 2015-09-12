package com.provectus.cardiff.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Created by artemvlasov on 11/09/15.
 */
@Configuration
@Profile("development")
public class DevelopmentDataSourceConfig {
    @Autowired
    private Environment env;

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        return dataSource;
    }
}
