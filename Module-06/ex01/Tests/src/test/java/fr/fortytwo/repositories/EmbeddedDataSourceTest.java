package fr.fortytwo.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

public class EmbeddedDataSourceTest {

    private EmbeddedDatabase embeddedDatabase;
    private DataSource dataSource;

    @BeforeEach
    public void init() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        dataSource = embeddedDatabase;
    }

    @AfterEach
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testGetConnectionIsNotNull() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection, "DataSource.getConnection() must not return null");
        connection.close();
    }

    @Test
    public void testConnectionIsNotClosed() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed(), "A freshly obtained connection must not be closed");
        connection.close();
    }

}
