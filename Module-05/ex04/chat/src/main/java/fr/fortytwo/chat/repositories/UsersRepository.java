package fr.fortytwo.chat.repositories;

import java.util.List;
import fr.fortytwo.chat.models.User;

public interface UsersRepository {
    List<User> findAll(final int page, final int size);
}
