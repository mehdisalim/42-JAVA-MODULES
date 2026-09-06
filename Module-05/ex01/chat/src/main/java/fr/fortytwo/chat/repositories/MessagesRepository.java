package fr.fortytwo.chat.repositories;

import java.util.Optional;

import fr.fortytwo.chat.models.Message;

public interface MessagesRepository {

    Optional<Message> findById(final Long id);
    
}