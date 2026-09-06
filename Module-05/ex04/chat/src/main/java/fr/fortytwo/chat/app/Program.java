package fr.fortytwo.chat.app;

import fr.fortytwo.chat.models.User;
import fr.fortytwo.chat.repositories.UsersRepository;
import fr.fortytwo.chat.repositories.UsersRepositoryJdbcImpl;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Statement;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;


public class Program {
    
    final private static String DB_URL = "jdbc:postgresql://localhost:5432/chat";
    final private static String USER_NAME = "user";
    final private static String USER_PASSWORD = "user";

    public static void main(String[] args) {
    
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(DB_URL);
        config.setUsername(USER_NAME);
        config.setPassword(USER_PASSWORD);

        
        
        HikariDataSource dataSource = new HikariDataSource(config);
        
        try {
            executeSqlScript(dataSource, "schema.sql");
            executeSqlScript(dataSource, "data.sql");
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }





        UsersRepository usersRepository =
        new UsersRepositoryJdbcImpl(dataSource);

        List<User> users = usersRepository.findAll(0, 3);

        for (User user : users) {

            System.out.println(
                "User: " + user.getLogin()
            );

            System.out.println("Created rooms:");

            user.getRooms()
                .forEach(room ->
                    System.out.println(
                        "  - " + room.getName()
                    )
                );

            System.out.println("Participated rooms:");

            user.getSharedRooms()
                .forEach(room ->
                    System.out.println(
                        "  - " + room.getName()
                    )
                );
        }



        dataSource.close();

        
    }

    private static void executeSqlScript(DataSource dataSource, String resourceName) throws Exception {
        try (
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            InputStream input = Program.class.getClassLoader().getResourceAsStream(resourceName)
        ) {
            if (input == null) {
                throw new RuntimeException(resourceName + " not found");
            }
    
            String sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);
    
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    statement.execute(query);
                }
            }
        }
    }

}
