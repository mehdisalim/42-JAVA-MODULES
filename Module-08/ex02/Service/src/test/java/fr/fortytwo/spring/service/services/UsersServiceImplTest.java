package fr.fortytwo.spring.service.services;

import fr.fortytwo.spring.service.config.TestApplicationConfig;
import fr.fortytwo.spring.service.services.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UsersServiceImplTest {

    @Test
    public void testSignUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                TestApplicationConfig.class);
        UsersService usersService = context.getBean(UsersService.class);

        String password = usersService.signUp("test@example.com");

        assertNotNull(password);
        assertFalse(password.isEmpty());

        context.close();
    }
}
