package fr.fortytwo.sockets.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

public class DatabaseInitializer {

    public DatabaseInitializer(DataSource dataSource) {

        ResourceDatabasePopulator populator =
                new ResourceDatabasePopulator(
                        new ClassPathResource("schema.sql")
                );

        DatabasePopulatorUtils.execute(
                populator,
                dataSource
        );
    }
}