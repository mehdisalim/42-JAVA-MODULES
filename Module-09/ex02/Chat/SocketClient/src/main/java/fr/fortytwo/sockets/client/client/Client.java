package fr.fortytwo.sockets.client.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private final ObjectMapper mapper = new ObjectMapper();

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            AtomicReference<Long> userId = new AtomicReference<>(null);
            AtomicReference<Long> roomId = new AtomicReference<>(null);
            
            Thread listenerThread = new Thread(() -> {
                try {
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            System.out.println("Connection closed by server.");
                            System.exit(0);
                            break;
                        }
                        
                        MessageDto msg = mapper.readValue(line, MessageDto.class);
                        
                        if ("auth_success".equals(msg.getType())) {
                            userId.set(msg.getFromId());
                        } else if ("room_joined".equals(msg.getType())) {
                            roomId.set(msg.getRoomId());
                            System.out.println(msg.getText() + " ---");
                        } else if ("prompt".equals(msg.getType()) || "message".equals(msg.getType())) {
                            if (msg.getText() != null) {
                                System.out.println(msg.getText());
                            }
                        } else if ("exit".equals(msg.getType())) {
                            if (msg.getText() != null) {
                                System.out.println(msg.getText());
                            }
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Listener thread error: " + e.getMessage());
                    System.exit(0);
                }
            });
            listenerThread.start();
            
            while (true) {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    
                    MessageDto outMsg = new MessageDto();
                    outMsg.setMessage(input);
                    if (userId.get() != null) {
                        outMsg.setFromId(userId.get());
                    }
                    if (roomId.get() != null) {
                        outMsg.setRoomId(roomId.get());
                    }
                    
                    writer.println(mapper.writeValueAsString(outMsg));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
