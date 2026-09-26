package fr.fortytwo.sockets.services;

import fr.fortytwo.sockets.models.Room;
import fr.fortytwo.sockets.repositories.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomsServiceImpl implements RoomsService {
    private final RoomsRepository roomsRepository;

    @Autowired
    public RoomsServiceImpl(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    @Override
    public void createRoom(String name, Long ownerId) {
        if (roomsRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Room already exists");
        }
        Room room = new Room(null, name, ownerId);
        roomsRepository.save(room);
    }

    @Override
    public List<Room> findAllRooms() {
        return roomsRepository.findAll();
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomsRepository.findById(id);
    }
}
