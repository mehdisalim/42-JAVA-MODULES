package fr.fortytwo.sockets.services;

import fr.fortytwo.sockets.models.Message;
import java.util.List;

public interface MessagesService {
    void sendMessage(Long authorId, Long roomId, String text);
    List<Message> getLastMessages(Long roomId, int limit);
}
