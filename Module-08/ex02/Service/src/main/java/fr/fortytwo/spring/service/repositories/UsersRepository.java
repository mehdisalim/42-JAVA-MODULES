package fr.fortytwo.spring.service.repositories;

import fr.fortytwo.spring.service.models.User;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
}
