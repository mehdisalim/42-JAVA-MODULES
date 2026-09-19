package edu.school21.spring.service.services;

import edu.school21.spring.service.models.User;
import edu.school21.spring.service.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplate") UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public String signUp(String email) {
        String temporaryPassword = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail(email);
        user.setPassword(temporaryPassword);
        usersRepository.save(user);
        return temporaryPassword;
    }
}
