package fr.fortytwo.repositories;

import fr.fortytwo.models.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {

    private final DataSource dataSource;

    public ProductsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ------------------------------------------------------------------
    // findAll
    // ------------------------------------------------------------------
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, price FROM product ORDER BY id";

        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed", e);
        }
        return products;
    }

    // ------------------------------------------------------------------
    // findById
    // ------------------------------------------------------------------
    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT id, name, price FROM product WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById failed for id=" + id, e);
        }
        return Optional.empty();
    }

    // ------------------------------------------------------------------
    // save (INSERT)
    // ------------------------------------------------------------------
    @Override
    public void save(Product product) {
        String sql = "INSERT INTO product (id, name, price) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, product.getId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save failed for " + product, e);
        }
    }

    // ------------------------------------------------------------------
    // update
    // ------------------------------------------------------------------
    @Override
    public void update(Product product) {
        String sql = "UPDATE product SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setLong(3, product.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update failed for " + product, e);
        }
    }

    // ------------------------------------------------------------------
    // delete
    // ------------------------------------------------------------------
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete failed for id=" + id, e);
        }
    }

    // ------------------------------------------------------------------
    // helper
    // ------------------------------------------------------------------
    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"));
    }
}
