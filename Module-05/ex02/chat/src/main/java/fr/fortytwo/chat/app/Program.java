package fr.fortytwo.chat.app;

import fr.fortytwo.chat.models.Message;
import fr.fortytwo.chat.models.User;
import fr.fortytwo.chat.models.Chatroom;
import fr.fortytwo.chat.repositories.MessagesRepository;
import fr.fortytwo.chat.repositories.MessagesRepositoryJdbcImpl;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Statement;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Optional;
import java.util.Scanner;
import java.util.ArrayList;
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




        
        User creator = new User(4L, "user", "user", new ArrayList(), new ArrayList());
        User author = creator;
        Chatroom room = new Chatroom(2L, "room", creator, new ArrayList());
        final Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Enter Your Message Here: ");
            if (!scanner.hasNextLine())
                break ;
            final String str = scanner.nextLine();
            Message message = new Message(null, author, room, str, LocalDateTime.now());
            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);
            messagesRepository.save(message);
            System.out.println(message.getId());
        }

        scanner.close();
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
