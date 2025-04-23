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
import ru.ifmo.monolith.config.properties.HotelsProperties;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "ru.ifmo.monolith.favorites",
        entityManagerFactoryRef = "favoritesEntityManager",
        transactionManagerRef = "transactionManager"
)
public class FavoritesConfig {

    private final HotelsProperties hotelsProperties;

    @Bean(name = "favoritesDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource favoritesDataSource() {
        var dataSource = new AtomikosDataSourceBean();
        var pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(hotelsProperties.getUrl());
        pgxaDataSource.setPassword(hotelsProperties.getPassword());
        pgxaDataSource.setUser(hotelsProperties.getUsername());
        dataSource.setUniqueResourceName("favoritesDataSource");
        dataSource.setXaDataSource(pgxaDataSource);
        return dataSource;
    }

    @Bean(name = "jpaVendorAdapterFavorites")
    public JpaVendorAdapter jpaVendorAdapterFavorites() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        return adapter;
    }

    @Bean(name = "favoritesEntityManager")
    @DependsOn({"atomikosJtaPlatform"})
    public LocalContainerEntityManagerFactoryBean favoritesEntityManager() {
        var entityManager = new LocalContainerEntityManagerFactoryBean();

        entityManager.setJpaVendorAdapter(jpaVendorAdapterFavorites());
        entityManager.setJtaDataSource(favoritesDataSource());
        entityManager.setJpaProperties(getProperties());
        entityManager.setPersistenceUnitName("favorites");
        entityManager.setPackagesToScan("ru.ifmo.monolith.favorites");

        return entityManager;
    }

    private Properties getProperties() {
        var properties = new Properties();
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