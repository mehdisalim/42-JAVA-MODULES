package edu.school21.spring.service.repositories;

import edu.school21.spring.service.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component("usersRepositoryJdbcTemplate")
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryJdbcTemplateImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(rs.getLong("identifier"), rs.getString("email"), rs.getString("password"));

    @Override
    public User findById(Long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE identifier = ?", userRowMapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public void save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPassword());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            entity.setIdentifier(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET email = ?, password = ? WHERE identifier = ?", entity.getEmail(), entity.getPassword(), entity.getIdentifier());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE identifier = ?", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}
