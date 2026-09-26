package fr.fortytwo.sockets.repositories;

import fr.fortytwo.sockets.models.Message;
import java.util.List;

public interface MessagesRepository extends CrudRepository<Message> {
    List<Message> findByRoomId(Long roomId, int limit);
}
