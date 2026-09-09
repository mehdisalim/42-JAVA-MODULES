package fr.fortytwo.repositories;

import fr.fortytwo.models.User;

public interface UsersRepository {

    User findByLogin(String login);

    void update(User user);
}
