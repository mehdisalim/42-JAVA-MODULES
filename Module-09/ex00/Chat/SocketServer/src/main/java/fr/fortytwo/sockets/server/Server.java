package fr.fortytwo.sockets.server;

import fr.fortytwo.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {
    private final UsersService usersService;

    @Autowired
    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                handleClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            writer.println("Hello from Server!");
            
            while (true) {
                String command = reader.readLine();
                if (command == null) break;
                
                if (command.trim().equalsIgnoreCase("signUp")) {
                    writer.println("Enter username:");
                    String username = reader.readLine();
                    if (username == null) break;
                    
                    writer.println("Enter password:");
                    String password = reader.readLine();
                    if (password == null) break;
                    
                    try {
                        usersService.signUp(username.trim(), password.trim());
                        writer.println("Successful!");
                    } catch (Exception e) {
                        writer.println("Failed: " + e.getMessage());
                    }
                    break;
                } else {
                    writer.println("Unknown command. Try 'signUp'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
