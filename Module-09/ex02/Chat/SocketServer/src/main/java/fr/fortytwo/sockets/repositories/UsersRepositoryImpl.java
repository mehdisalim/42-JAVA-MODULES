package fr.fortytwo.sockets.repositories;

import fr.fortytwo.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        initDatabase();
    }
    
    private void initDatabase() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(255) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL)");
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password")
    );

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", userRowMapper, id);
        return users.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update("INSERT INTO users (username, password) VALUES (?, ?)",
                entity.getUsername(), entity.getPassword());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET username = ?, password = ? WHERE id = ?",
                entity.getUsername(), entity.getPassword(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE username = ?", userRowMapper, username);
        return users.stream().findFirst();
    }
}
