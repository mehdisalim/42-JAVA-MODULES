package fr.fortytwo.sockets.services;

import fr.fortytwo.sockets.models.User;
import java.util.Optional;

public interface UsersService {
    void signUp(String username, String password);
    Optional<User> signIn(String username, String password);
    Optional<User> findById(Long id);
}
