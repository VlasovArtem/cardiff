package com.provectus.cardiff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

/**
 * Created by artemvlasov on 11/09/15.
 */
@Configuration
@Profile("production")
public class ProductionDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        final JndiDataSourceLookup
                dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        return dsLookup.getDataSource("java:/cardiff");
    }

}
