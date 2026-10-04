package fr.fortytwo.sockets.services;

public interface MessagesService {
    void saveMessage(Long authorId, String text);
}
