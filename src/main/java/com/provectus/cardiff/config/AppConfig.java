package com.provectus.cardiff.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by artemvlasov on 20/08/15.
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
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setPackagesToScan("com.provectus.cardiff");
        bean.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hbm2ddl.auto"));
        properties.put("hibernate.show_sql", env.getProperty("show_sql"));
        bean.setJpaProperties(properties);
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return bean;
    }
}
