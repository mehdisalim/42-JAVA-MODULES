package fr.fortytwo.spring.service.application;

import fr.fortytwo.spring.service.config.ApplicationConfig;
import fr.fortytwo.spring.service.services.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UsersService usersService = context.getBean(UsersService.class);
        System.out.println("Generated password: " + usersService.signUp("user_test@example.com"));
    }
}
