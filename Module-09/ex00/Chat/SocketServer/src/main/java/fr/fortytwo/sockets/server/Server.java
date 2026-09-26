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
                // "Server shall support connecting a single client". Let's assume it breaks after handling one.
                // Wait, it says "Server shall support connecting a single client and be made as a separate Maven project." 
                // Maybe it means it just doesn't need multithreading? "support connecting a single client" implies 
                // no threads are needed, it just handles one at a time. I'll just not break, so we can test multiple clients one after another.
                // Actually the requirement says "Server shall support connecting a single client", which implies no multi-user chat yet.
                // So looping is fine.
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
