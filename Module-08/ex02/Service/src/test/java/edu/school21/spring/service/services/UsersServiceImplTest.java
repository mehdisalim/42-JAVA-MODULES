package edu.school21.spring.service.services;

import edu.school21.spring.service.config.TestApplicationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UsersServiceImplTest {

    @Test
    public void testSignUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestApplicationConfig.class);
        UsersService usersService = context.getBean(UsersService.class);

        String password = usersService.signUp("test@example.com");

        assertNotNull(password);
        assertFalse(password.isEmpty());

        context.close();
    }
}
