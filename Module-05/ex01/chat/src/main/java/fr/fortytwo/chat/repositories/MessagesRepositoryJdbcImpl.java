package fr.fortytwo.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import fr.fortytwo.chat.models.Chatroom;
import fr.fortytwo.chat.models.Message;
import fr.fortytwo.chat.models.User;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {

    final private DataSource dataSource;

    public MessagesRepositoryJdbcImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Message> findById(final Long id) {
    
        final String sql = """
            SELECT
                m.id AS message_id,
                m.text,
                m.created_at,
    
                u.id AS user_id,
                u.login,
                u.password,
    
                c.id AS room_id,
                c.name
            FROM messages m
            JOIN users u
                ON m.author_id = u.id
            JOIN chatrooms c
                ON m.room_id = c.id
            WHERE m.id = ?;
            """;
    
        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
    
            statement.setLong(1, id);
    
            try (ResultSet rs = statement.executeQuery()) {
    
                if (!rs.next()) {
                    return Optional.empty();
                }
    
                User author = new User(
                        rs.getLong("user_id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        null,
                        null
                );
    
                Chatroom room = new Chatroom(
                        rs.getLong("room_id"),
                        rs.getString("name"),
                        null,
                        null
                );
    
                Message message = new Message(
                        rs.getLong("message_id"),
                        author,
                        room,
                        rs.getString("text"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
    
                return Optional.of(message);
            }
    
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find message with id = " + id, e);
        }
    }
    
}
