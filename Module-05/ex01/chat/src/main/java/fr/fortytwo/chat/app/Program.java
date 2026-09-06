package fr.fortytwo.chat.app;

import fr.fortytwo.chat.models.Message;
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

            final Scanner scan = new Scanner(System.in);
            System.out.println("Enter a message ID");
            System.out.print("-> ");
            if (scan.hasNextLong()) {
                
                final long messageId = scan.nextLong();
                final MessagesRepository repo = new MessagesRepositoryJdbcImpl(dataSource);
                final Optional<Message> message = repo.findById(messageId);
                System.out.println(message.get());
            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
