package fr.fortytwo.sockets.services;

import fr.fortytwo.sockets.models.Message;
import fr.fortytwo.sockets.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class MessagesServiceImpl implements MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void sendMessage(Long authorId, Long roomId, String text) {
        Message message = new Message(null, authorId, roomId, text, new Timestamp(System.currentTimeMillis()));
        messagesRepository.save(message);
    }

    @Override
    public List<Message> getLastMessages(Long roomId, int limit) {
        return messagesRepository.findByRoomId(roomId, limit);
    }
}
