package fr.fortytwo.sockets.services;

import fr.fortytwo.sockets.models.Message;
import fr.fortytwo.sockets.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class MessagesServiceImpl implements MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void saveMessage(Long authorId, String text) {
        Message message = new Message(null, authorId, text, new Timestamp(System.currentTimeMillis()));
        messagesRepository.save(message);
    }
}
