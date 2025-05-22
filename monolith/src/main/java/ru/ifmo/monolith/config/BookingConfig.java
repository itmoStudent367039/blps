package ru.ifmo.monolith.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.RequiredArgsConstructor;
import org.postgresql.xa.PGXADataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import ru.ifmo.monolith.config.properties.BookingProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "ru.ifmo.monolith.booking",
        entityManagerFactoryRef = "bookingEntityManager",
        transactionManagerRef = "transactionManager"
)
public class BookingConfig {

    private final BookingProperties bookingProperties;

    @Bean(name = "bookingDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource bookingDataSource() {
        var dataSource = new AtomikosDataSourceBean();
        var pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(bookingProperties.getUrl());
        pgxaDataSource.setPassword(bookingProperties.getPassword());
        pgxaDataSource.setUser(bookingProperties.getUsername());
        dataSource.setUniqueResourceName("bookingDataSource");
        dataSource.setXaDataSource(pgxaDataSource);
        return dataSource;
    }

    @Bean(name = "jpaVendorAdapterBooking")
    public JpaVendorAdapter jpaVendorAdapterBooking() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        return adapter;
    }

    @Bean(name = "bookingEntityManager")
    @DependsOn({"atomikosJtaPlatform"})
    public LocalContainerEntityManagerFactoryBean bookingEntityManager() {
        var properties = new Properties();
        properties.putAll(getProperties());

        var entityManager = new LocalContainerEntityManagerFactoryBean();

        entityManager.setJpaVendorAdapter(jpaVendorAdapterBooking());
        entityManager.setJtaDataSource(bookingDataSource());
        entityManager.setJpaProperties(properties);
        entityManager.setPersistenceUnitName("booking");
        entityManager.setPackagesToScan("ru.ifmo.monolith.booking");

        return entityManager;
    }

    private Map<String, String> getProperties() {
        var properties = new HashMap<String, String>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory");
        properties.put("hibernate.transaction.jta.platform", "ru.ifmo.monolith.config.AtomikosJtaPlatform");
        properties.put("jakarta.persistence.transactionType", "JTA");
        return properties;
    }
}