package fr.fortytwo.sockets.repositories;

import fr.fortytwo.sockets.models.Room;

import java.util.Optional;

public interface RoomsRepository extends CrudRepository<Room> {
    Optional<Room> findByName(String name);
}
