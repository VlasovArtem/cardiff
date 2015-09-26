package com.provectus.cardiff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by artemvlasov on 20/08/15.
 * Base configuration for database connection and enabling spring data jpa repositories
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.provectus.cardiff.persistence.repository"}, entityManagerFactoryRef =
        "entityManagerFactory")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Autowired
    private Environment env;
    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setPackagesToScan("com.provectus.cardiff");
        bean.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("show_sql"));
        bean.setJpaProperties(properties);
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return bean;
    }
}
