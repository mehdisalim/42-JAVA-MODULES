package fr.fortytwo.sockets.services;

import fr.fortytwo.sockets.models.Room;
import java.util.List;
import java.util.Optional;

public interface RoomsService {
    void createRoom(String name, Long ownerId);
    List<Room> findAllRooms();
    Optional<Room> findById(Long id);
}
