package fr.fortytwo.sockets.server;

import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.services.MessagesService;
import fr.fortytwo.sockets.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final UsersService usersService;
    private final MessagesService messagesService;
    private final Server server;
    private PrintWriter writer;
    private User currentUser;

    public ClientHandler(Socket socket, UsersService usersService, MessagesService messagesService, Server server) {
        this.socket = socket;
        this.usersService = usersService;
        this.messagesService = messagesService;
        this.server = server;
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("Hello from Server!");

            String command = reader.readLine();
            if (command == null) return;
            command = command.trim();

            if (command.equalsIgnoreCase("signUp")) {
                handleSignUp(reader);
            } else if (command.equalsIgnoreCase("signIn")) {
                if (handleSignIn(reader)) {
                    server.registerClient(this);
                    handleChat(reader);
                }
            } else {
                writer.println("Unknown command. Connection closed.");
            }
        } catch (IOException e) {
            // Client disconnected
        } finally {
            server.unregisterClient(this);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void handleSignUp(BufferedReader reader) throws IOException {
        writer.println("Enter username:");
        String username = reader.readLine();
        if (username == null) return;

        writer.println("Enter password:");
        String password = reader.readLine();
        if (password == null) return;

        try {
            usersService.signUp(username.trim(), password.trim());
            writer.println("Successful!");
        } catch (Exception e) {
            writer.println("Failed: " + e.getMessage());
        }
    }

    private boolean handleSignIn(BufferedReader reader) throws IOException {
        writer.println("Enter username:");
        String username = reader.readLine();
        if (username == null) return false;

        writer.println("Enter password:");
        String password = reader.readLine();
        if (password == null) return false;

        Optional<User> userOpt = usersService.signIn(username.trim(), password.trim());
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            writer.println("Start messaging");
            return true;
        } else {
            writer.println("Authentication failed. Connection closed.");
            return false;
        }
    }

    private void handleChat(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            if (line.equalsIgnoreCase("Exit")) {
                writer.println("You have left the chat.");
                break;
            }

            messagesService.saveMessage(currentUser.getId(), line);
            server.broadcast(currentUser.getUsername() + ": " + line);
        }
    }
}
