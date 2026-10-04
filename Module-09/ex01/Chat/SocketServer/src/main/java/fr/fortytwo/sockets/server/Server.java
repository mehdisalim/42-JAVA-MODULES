package fr.fortytwo.sockets.server;

import fr.fortytwo.sockets.services.MessagesService;
import fr.fortytwo.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Server {
    private final UsersService usersService;
    private final MessagesService messagesService;
    private final List<ClientHandler> activeClients = new CopyOnWriteArrayList<>();

    @Autowired
    public Server(UsersService usersService, MessagesService messagesService) {
        this.usersService = usersService;
        this.messagesService = messagesService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, usersService, messagesService, this);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerClient(ClientHandler handler) {
        activeClients.add(handler);
    }

    public void unregisterClient(ClientHandler handler) {
        activeClients.remove(handler);
    }

    public void broadcast(String message) {
        for (ClientHandler handler : activeClients) {
            handler.sendMessage(message);
        }
    }
}
