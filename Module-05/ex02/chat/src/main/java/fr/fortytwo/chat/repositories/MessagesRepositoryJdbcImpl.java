package fr.fortytwo.chat.repositories;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import fr.fortytwo.chat.models.Chatroom;
import fr.fortytwo.chat.models.Message;
import fr.fortytwo.chat.models.User;

import fr.fortytwo.chat.exceptions.NotSavedSubEntityException;
import fr.fortytwo.chat.exceptions.UnanbleToSaveException;


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




    @Override
    public void save(final Message message) {

        if (message == null
                || message.getAuthor() == null
                || message.getRoom() == null
                || message.getAuthor().getId() == null
                || message.getRoom().getId() == null) {

            throw new NotSavedSubEntityException("Message author or chatroom is not saved");
        }

        final String checkUserSql = """
            SELECT EXISTS (
                SELECT 1 FROM users WHERE id = ?
            );
            """;

        final String checkRoomSql = """
            SELECT EXISTS (
                SELECT 1 FROM chatrooms WHERE id = ?
            );
            """;

        final String saveMessageSql = """
            INSERT INTO messages (author_id, room_id, text, created_at)
            VALUES (?, ?, ?, ?);
            """;

        try (
            Connection connection = dataSource.getConnection()
        ) {

            // Check if author exists
            try (PreparedStatement statement = connection.prepareStatement(checkUserSql)) {

                statement.setLong(1, message.getAuthor().getId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();

                    if (!resultSet.getBoolean(1)) {
                        throw new NotSavedSubEntityException(
                                "Author does not exist in database"
                        );
                    }
                }
            }

            // Check if chatroom exists
            try (PreparedStatement statement = connection.prepareStatement(checkRoomSql)) {

                statement.setLong(1, message.getRoom().getId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();

                    if (!resultSet.getBoolean(1)) {
                        throw new NotSavedSubEntityException(
                                "Chatroom does not exist in database"
                        );
                    }
                }
            }

            // Save message
            try (
                PreparedStatement statement = connection.prepareStatement(saveMessageSql, Statement.RETURN_GENERATED_KEYS)
            ) {

                statement.setLong(1, message.getAuthor().getId());
                statement.setLong(2, message.getRoom().getId());
                statement.setString(3, message.getText());
                statement.setTimestamp(4, java.sql.Timestamp.valueOf(message.getCreatedAt()));

                statement.executeUpdate();

                // Get generated ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        message.setId(generatedKeys.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new UnanbleToSaveException("Unable to save message", e);
        }
    }
    
}
