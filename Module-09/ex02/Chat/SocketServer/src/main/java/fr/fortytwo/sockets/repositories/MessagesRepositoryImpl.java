package fr.fortytwo.sockets.repositories;

import fr.fortytwo.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class MessagesRepositoryImpl implements MessagesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        initDatabase();
    }

    private void initDatabase() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS messages (" +
                "id SERIAL PRIMARY KEY, " +
                "author_id INT REFERENCES users(id), " +
                "room_id INT REFERENCES rooms(id), " +
                "text TEXT NOT NULL, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }

    private final RowMapper<Message> rowMapper = (rs, rowNum) -> new Message(
            rs.getLong("id"),
            rs.getLong("author_id"),
            rs.getLong("room_id"),
            rs.getString("text"),
            rs.getTimestamp("timestamp")
    );

    @Override
    public Optional<Message> findById(Long id) {
        List<Message> messages = jdbcTemplate.query("SELECT * FROM messages WHERE id = ?", rowMapper, id);
        return messages.stream().findFirst();
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query("SELECT * FROM messages", rowMapper);
    }

    @Override
    public void save(Message entity) {
        jdbcTemplate.update("INSERT INTO messages (author_id, room_id, text, timestamp) VALUES (?, ?, ?, ?)",
                entity.getAuthorId(), entity.getRoomId(), entity.getText(), entity.getTimestamp());
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update("UPDATE messages SET text = ? WHERE id = ?",
                entity.getText(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM messages WHERE id = ?", id);
    }

    @Override
    public List<Message> findByRoomId(Long roomId, int limit) {
        return jdbcTemplate.query("SELECT * FROM messages WHERE room_id = ? ORDER BY timestamp DESC LIMIT ?", 
                rowMapper, roomId, limit);
    }
}
