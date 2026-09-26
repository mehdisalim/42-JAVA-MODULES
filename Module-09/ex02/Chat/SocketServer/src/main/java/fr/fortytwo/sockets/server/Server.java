package fr.fortytwo.sockets.server;

import fr.fortytwo.sockets.services.MessagesService;
import fr.fortytwo.sockets.services.RoomsService;
import fr.fortytwo.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {
    private final UsersService usersService;
    private final RoomsService roomsService;
    private final MessagesService messagesService;

    @Autowired
    public Server(UsersService usersService, RoomsService roomsService, MessagesService messagesService) {
        this.usersService = usersService;
        this.roomsService = roomsService;
        this.messagesService = messagesService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, usersService, roomsService, messagesService);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
