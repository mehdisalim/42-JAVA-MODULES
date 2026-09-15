package fr.fortytwo.orm;

import fr.fortytwo.orm.manager.OrmManager;
import fr.fortytwo.orm.models.User;
import org.h2.jdbcx.JdbcDataSource;

public class Program {
    public static void main(String[] args) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        OrmManager manager = new OrmManager(dataSource);

        System.out.println("\n--- SAVE ---");
        User user1 = new User(null, "John", "Doe", 25);
        User user2 = new User(null, "Jane", "Smith", 30);
        manager.save(user1);
        manager.save(user2);

        System.out.println("\n--- FIND BY ID ---");
        User foundUser = manager.findById(1L, User.class);
        System.out.println("Found: " + foundUser);

        System.out.println("\n--- UPDATE ---");
        if (foundUser != null) {
            foundUser.setFirstName("Johnny");
            foundUser.setAge(null); // Testing setting values to NULL
            manager.update(foundUser);
        }

        System.out.println("\n--- VERIFY UPDATE ---");
        User updatedUser = manager.findById(1L, User.class);
        System.out.println("Updated: " + updatedUser);
    }
}