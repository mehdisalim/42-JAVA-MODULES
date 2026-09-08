package fr.fortytwo.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.*;

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

    // -----------------------------------------------------------------------
    // 1. Basic connectivity
    // -----------------------------------------------------------------------

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

    // -----------------------------------------------------------------------
    // 2. Schema / table existence
    // -----------------------------------------------------------------------

    @Test
    public void testProductTableExists() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            // HSQLDB stores names in uppercase by default
            ResultSet tables = meta.getTables(null, null, "PRODUCT", new String[]{"TABLE"});
            assertTrue(tables.next(), "Table 'product' must exist in the schema");
        }
    }

    // -----------------------------------------------------------------------
    // 3. Row count
    // -----------------------------------------------------------------------

    @Test
    public void testProductTableHasFiveRows() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM product")) {
            assertTrue(rs.next());
            assertEquals(5, rs.getInt(1), "Seed data must contain exactly 5 products");
        }
    }

    // -----------------------------------------------------------------------
    // 4. Data integrity checks
    // -----------------------------------------------------------------------

    @Test
    public void testFirstProductByIdIsApple() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT name, price FROM product WHERE id = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Product with id=1 must exist");
                assertEquals("Apple", rs.getString("name"));
                assertEquals(0.99, rs.getDouble("price"), 0.001);
            }
        }
    }

    @Test
    public void testAllProductPricesArePositive() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, price FROM product")) {
            while (rs.next()) {
                assertTrue(rs.getDouble("price") > 0,
                        "Price for product id=" + rs.getInt("id") + " must be positive");
            }
        }
    }

    @Test
    public void testAllProductNamesAreNotEmpty() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM product")) {
            while (rs.next()) {
                String name = rs.getString("name");
                assertNotNull(name, "Name must not be null for id=" + rs.getInt("id"));
                assertFalse(name.isBlank(), "Name must not be blank for id=" + rs.getInt("id"));
            }
        }
    }

    // -----------------------------------------------------------------------
    // 5. Write operations (INSERT / UPDATE / DELETE)
    // -----------------------------------------------------------------------

    @Test
    public void testInsertNewProduct() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO product (id, name, price) VALUES (?, ?, ?)")) {
            ps.setInt(1, 99);
            ps.setString(2, "Mango");
            ps.setDouble(3, 3.49);
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "INSERT must affect exactly 1 row");
        }

        // verify the row is now present
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM product")) {
            assertTrue(rs.next());
            assertEquals(6, rs.getInt(1), "After INSERT the table must have 6 rows");
        }
    }

    @Test
    public void testUpdateProductPrice() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE product SET price = ? WHERE id = ?")) {
            ps.setDouble(1, 1.99);
            ps.setInt(2, 2);
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "UPDATE must affect exactly 1 row");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT price FROM product WHERE id = ?")) {
            ps.setInt(1, 2);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1.99, rs.getDouble("price"), 0.001, "Price must be updated");
            }
        }
    }

    @Test
    public void testDeleteProduct() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM product WHERE id = ?")) {
            ps.setInt(1, 5);
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "DELETE must affect exactly 1 row");
        }

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM product")) {
            assertTrue(rs.next());
            assertEquals(4, rs.getInt(1), "After DELETE the table must have 4 rows");
        }
    }
}
