package fr.fortytwo.services;

import fr.fortytwo.exceptions.AlreadyAuthenticatedException;
import fr.fortytwo.models.User;
import fr.fortytwo.repositories.UsersRepository;

public class UsersServiceImpl {

    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean authenticate(String login, String password) {
        User user = usersRepository.findByLogin(login);

        if (user.isAuthenticated()) {
            throw new AlreadyAuthenticatedException(
                    "User '" + login + "' is already authenticated.");
        }

        if (user.getPassword().equals(password)) {
            user.setAuthenticated(true);
            usersRepository.update(user);
            return true;
        }

        return false;
    }
}
