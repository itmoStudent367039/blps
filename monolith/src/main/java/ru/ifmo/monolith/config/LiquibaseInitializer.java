package ru.ifmo.monolith.config;

import jakarta.annotation.PostConstruct;
import liquibase.command.CommandScope;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LiquibaseInitializer {

    private static final String DB_CHANGELOG_DB_CHANGELOG_MASTER_YAML = "db/changelog/db.changelog-master.yaml";

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @PostConstruct
    public void runLiquibase() {
        try {
            new CommandScope("update")
                    .addArgumentValue("changelogFile", DB_CHANGELOG_DB_CHANGELOG_MASTER_YAML)
                    .addArgumentValue("url", databaseUrl)
                    .addArgumentValue("username", databaseUsername)
                    .addArgumentValue("password", databasePassword)
                    .addArgumentValue("contexts", "")
                    .addArgumentValue("labels", "")
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to run Liquibase migration", e);
        }
    }
}
