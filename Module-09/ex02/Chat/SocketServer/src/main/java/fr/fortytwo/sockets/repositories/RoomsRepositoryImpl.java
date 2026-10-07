package fr.fortytwo.sockets.repositories;

import fr.fortytwo.sockets.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class RoomsRepositoryImpl implements RoomsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        initDatabase();
    }

    private void initDatabase() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) UNIQUE NOT NULL, " +
                "owner_id INT REFERENCES users(id))");
    }

    private final RowMapper<Room> rowMapper = (rs, rowNum) -> new Room(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getLong("owner_id")
    );

    @Override
    public Optional<Room> findById(Long id) {
        List<Room> rooms = jdbcTemplate.query("SELECT * FROM rooms WHERE id = ?", rowMapper, id);
        return rooms.stream().findFirst();
    }

    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query("SELECT * FROM rooms", rowMapper);
    }

    @Override
    public void save(Room entity) {
        jdbcTemplate.update("INSERT INTO rooms (name, owner_id) VALUES (?, ?)",
                entity.getName(), entity.getOwnerId());
    }

    @Override
    public void update(Room entity) {
        jdbcTemplate.update("UPDATE rooms SET name = ?, owner_id = ? WHERE id = ?",
                entity.getName(), entity.getOwnerId(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM rooms WHERE id = ?", id);
    }

    @Override
    public Optional<Room> findByName(String name) {
        List<Room> rooms = jdbcTemplate.query("SELECT * FROM rooms WHERE name = ?", rowMapper, name);
        return rooms.stream().findFirst();
    }
}
