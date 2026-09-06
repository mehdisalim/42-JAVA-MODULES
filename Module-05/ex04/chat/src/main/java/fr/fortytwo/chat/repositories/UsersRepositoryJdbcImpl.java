package fr.fortytwo.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import fr.fortytwo.chat.models.Chatroom;
import fr.fortytwo.chat.models.User;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    private final DataSource dataSource;

    public UsersRepositoryJdbcImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(final int page, final int size) {

        final String sql = """
            WITH paginated_users AS (
                SELECT id, login, password
                FROM users
                ORDER BY id
                LIMIT ?
                OFFSET ?
            )
            SELECT
                u.id AS user_id,
                u.login AS user_login,
                u.password AS user_password,

                created_room.id AS created_room_id,
                created_room.name AS created_room_name,

                participated_room.id AS participated_room_id,
                participated_room.name AS participated_room_name

            FROM paginated_users u

            LEFT JOIN chatrooms created_room
                ON created_room.owner_id = u.id

            LEFT JOIN chatroom_users cu
                ON cu.user_id = u.id

            LEFT JOIN chatrooms participated_room
                ON participated_room.id = cu.chatroom_id

            ORDER BY u.id;
            """;

        Map<Long, User> users = new LinkedHashMap<>();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, size);
            statement.setInt(2, page * size);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Long userId =
                        resultSet.getLong("user_id");

                    User user = users.get(userId);

                    if (user == null) {

                        user = new User(
                            userId,
                            resultSet.getString("user_login"),
                            resultSet.getString("user_password"),
                            new ArrayList<>(),
                            new ArrayList<>()
                        );

                        users.put(userId, user);
                    }

                    // Created room
                    Long createdRoomId =
                        resultSet.getObject(
                            "created_room_id",
                            Long.class
                        );

                    if (createdRoomId != null) {

                        Chatroom createdRoom =
                            new Chatroom(
                                createdRoomId,
                                resultSet.getString(
                                    "created_room_name"
                                ),
                                null,
                                new ArrayList<>()
                            );

                        boolean alreadyExists =
                            user.getRooms()
                                .stream()
                                .anyMatch(
                                    room ->
                                        room.getId()
                                            .equals(createdRoomId)
                                );

                        if (!alreadyExists) {
                            user.getRooms()
                                .add(createdRoom);
                        }
                    }

                    // Participated room
                    Long participatedRoomId =
                        resultSet.getObject(
                            "participated_room_id",
                            Long.class
                        );

                    if (participatedRoomId != null) {

                        Chatroom participatedRoom =
                            new Chatroom(
                                participatedRoomId,
                                resultSet.getString(
                                    "participated_room_name"
                                ),
                                null,
                                new ArrayList<>()
                            );

                        boolean alreadyExists =
                            user.getSharedRooms()
                                .stream()
                                .anyMatch(
                                    room ->
                                        room.getId()
                                            .equals(participatedRoomId)
                                );

                        if (!alreadyExists) {
                            user.getSharedRooms()
                                .add(participatedRoom);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Unable to retrieve users",
                e
            );
        }

        return new ArrayList<>(users.values());
    }
}