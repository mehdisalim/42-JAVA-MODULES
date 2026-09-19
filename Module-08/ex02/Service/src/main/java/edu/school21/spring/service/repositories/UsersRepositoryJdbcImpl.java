package edu.school21.spring.service.repositories;

import edu.school21.spring.service.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("usersRepositoryJdbc")
public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource dataSource;

    @Autowired
    public UsersRepositoryJdbcImpl(@Qualifier("driverManagerDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE identifier = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getLong("identifier"), resultSet.getString("email"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("identifier"), resultSet.getString("email"), resultSet.getString("password")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return users;
    }

    @Override
    public void save(User entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setIdentifier(keys.getLong(1));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET email = ?, password = ? WHERE identifier = ?")) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setLong(3, entity.getIdentifier());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE identifier = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getLong("identifier"), resultSet.getString("email"), resultSet.getString("password")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }
}
