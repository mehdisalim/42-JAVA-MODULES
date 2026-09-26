package fr.fortytwo.sockets.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fortytwo.sockets.models.Message;
import fr.fortytwo.sockets.models.Room;
import fr.fortytwo.sockets.models.User;
import fr.fortytwo.sockets.services.MessagesService;
import fr.fortytwo.sockets.services.RoomsService;
import fr.fortytwo.sockets.services.UsersService;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final UsersService usersService;
    private final RoomsService roomsService;
    private final MessagesService messagesService;
    private final ObjectMapper mapper = new ObjectMapper();
    private PrintWriter writer;
    
    // Global list of active handlers to broadcast messages
    private static final ConcurrentHashMap<Long, ClientHandler> activeClients = new ConcurrentHashMap<>();

    private User currentUser = null;
    private Room currentRoom = null;

    public ClientHandler(Socket socket, UsersService usersService, RoomsService roomsService, MessagesService messagesService) {
        this.socket = socket;
        this.usersService = usersService;
        this.roomsService = roomsService;
        this.messagesService = messagesService;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            sendPrompt("Hello from Server!\n1. signIn\n2. signUp\n3. Exit\n>");

            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                MessageDto dto;
                try {
                    dto = mapper.readValue(line, MessageDto.class);
                } catch (Exception e) {
                    continue;
                }
                
                String cmd = dto.getMessage();
                if (cmd == null) cmd = "";
                cmd = cmd.trim();

                if (currentUser == null) {
                    handleAuthMenu(cmd, reader);
                } else if (currentRoom == null) {
                    handleRoomMenu(cmd, reader);
                } else {
                    handleChat(cmd);
                }
            }

        } catch (Exception e) {
            System.err.println("Client handler exception: " + e.getMessage());
        } finally {
            if (currentUser != null) {
                activeClients.remove(currentUser.getId());
            }
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private void sendPrompt(String text) {
        MessageDto dto = new MessageDto();
        dto.setType("prompt");
        dto.setText(text);
        send(dto);
    }
    
    private void sendExit(String text) {
        MessageDto dto = new MessageDto();
        dto.setType("exit");
        dto.setText(text);
        send(dto);
    }

    private void send(MessageDto dto) {
        try {
            if (writer != null) {
                writer.println(mapper.writeValueAsString(dto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readNextCommand(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) return null;
        MessageDto dto = mapper.readValue(line, MessageDto.class);
        return dto.getMessage() != null ? dto.getMessage().trim() : "";
    }

    private void handleAuthMenu(String cmd, BufferedReader reader) throws IOException {
        if ("1".equals(cmd) || "signIn".equalsIgnoreCase(cmd)) {
            sendPrompt("Enter username:\n>");
            String username = readNextCommand(reader);
            if (username == null) return;

            sendPrompt("Enter password:\n>");
            String password = readNextCommand(reader);
            if (password == null) return;

            Optional<User> userOpt = usersService.signIn(username, password);
            if (userOpt.isPresent()) {
                currentUser = userOpt.get();
                activeClients.put(currentUser.getId(), this);
                MessageDto authSuccess = new MessageDto();
                authSuccess.setType("auth_success");
                authSuccess.setFromId(currentUser.getId());
                send(authSuccess);
                
                showRoomMenu();
            } else {
                sendExit("signIn failed! Connection closed.");
            }
        } else if ("2".equals(cmd) || "signUp".equalsIgnoreCase(cmd)) {
            sendPrompt("Enter username:\n>");
            String username = readNextCommand(reader);
            if (username == null) return;

            sendPrompt("Enter password:\n>");
            String password = readNextCommand(reader);
            if (password == null) return;

            try {
                usersService.signUp(username, password);
                sendPrompt("Successful!\n1. signIn\n2. signUp\n3. Exit\n>");
            } catch (Exception e) {
                sendPrompt("Failed: " + e.getMessage() + "\n1. signIn\n2. signUp\n3. Exit\n>");
            }
        } else if ("3".equals(cmd) || "Exit".equalsIgnoreCase(cmd)) {
            sendExit("Bye!");
        } else {
            sendPrompt("Unknown command.\n1. signIn\n2. signUp\n3. Exit\n>");
        }
    }

    private void showRoomMenu() {
        sendPrompt("1. Create room\n2. Choose room\n3. Exit\n>");
    }

    private void handleRoomMenu(String cmd, BufferedReader reader) throws IOException {
        if ("1".equals(cmd)) {
            sendPrompt("Enter room name:\n>");
            String roomName = readNextCommand(reader);
            if (roomName == null) return;

            try {
                roomsService.createRoom(roomName, currentUser.getId());
                showRoomMenu();
            } catch (Exception e) {
                sendPrompt("Failed to create room: " + e.getMessage() + "\n1. Create room\n2. Choose room\n3. Exit\n>");
            }
        } else if ("2".equals(cmd)) {
            List<Room> rooms = roomsService.findAllRooms();
            if (rooms.isEmpty()) {
                sendPrompt("No rooms available.\n1. Create room\n2. Choose room\n3. Exit\n>");
                return;
            }
            
            StringBuilder sb = new StringBuilder("Rooms:\n");
            for (int i = 0; i < rooms.size(); i++) {
                sb.append(i + 1).append(". ").append(rooms.get(i).getName()).append("\n");
            }
            sb.append(rooms.size() + 1).append(". Exit\n>");
            sendPrompt(sb.toString());

            String choiceStr = readNextCommand(reader);
            if (choiceStr == null) return;
            try {
                int choice = Integer.parseInt(choiceStr);
                if (choice > 0 && choice <= rooms.size()) {
                    currentRoom = rooms.get(choice - 1);
                    
                    MessageDto joinDto = new MessageDto();
                    joinDto.setType("room_joined");
                    joinDto.setRoomId(currentRoom.getId());
                    joinDto.setText(currentRoom.getName());
                    send(joinDto);

                    List<Message> lastMessages = messagesService.getLastMessages(currentRoom.getId(), 30);
                    for (int i = lastMessages.size() - 1; i >= 0; i--) {
                        Message m = lastMessages.get(i);
                        Optional<User> authorOpt = usersService.findById(m.getAuthorId());
                        String authorName = authorOpt.isPresent() ? authorOpt.get().getUsername() : "Unknown";
                        sendPrompt(authorName + ": " + m.getText());
                    }
                    sendPrompt(">");
                } else if (choice == rooms.size() + 1) {
                    sendExit("Bye!");
                } else {
                    showRoomMenu();
                }
            } catch (NumberFormatException e) {
                showRoomMenu();
            }
        } else if ("3".equals(cmd) || "Exit".equalsIgnoreCase(cmd)) {
            sendExit("Bye!");
        } else {
            showRoomMenu();
        }
    }

    private void handleChat(String cmd) {
        if ("Exit".equalsIgnoreCase(cmd)) {
            sendExit("You have left the chat.");
            return;
        }

        messagesService.sendMessage(currentUser.getId(), currentRoom.getId(), cmd);

        MessageDto broadcast = new MessageDto();
        broadcast.setType("message");
        broadcast.setText(currentUser.getUsername() + ": " + cmd + "\n>");

        for (ClientHandler handler : activeClients.values()) {
            if (handler.currentRoom != null && handler.currentRoom.getId().equals(this.currentRoom.getId())) {
                handler.send(broadcast);
            }
        }
    }
}
