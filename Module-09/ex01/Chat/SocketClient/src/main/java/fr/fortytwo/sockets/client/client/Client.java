package fr.fortytwo.sockets.client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String serverAddress;
    private final int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Step 1: Authentication / registration phase
            while (true) {
                String serverMessage = reader.readLine();
                if (serverMessage == null) {
                    return;
                }

                System.out.println(serverMessage);

                if (serverMessage.equals("Successful!") || serverMessage.startsWith("Failed:") ||
                    serverMessage.contains("failed") || serverMessage.contains("closed")) {
                    return;
                }

                if (serverMessage.equals("Start messaging")) {
                    break;
                }

                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    return;
                }
                String userInput = scanner.nextLine();
                writer.println(userInput);
            }

            // Step 2: Messaging phase
            Thread listenerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        if (line.equals("You have left the chat.")) {
                            System.exit(0);
                        }
                    }
                    System.exit(0);
                } catch (IOException e) {
                    System.exit(0);
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

            while (scanner.hasNextLine()) {
                System.out.print("> ");
                String userInput = scanner.nextLine();
                writer.println(userInput);
                if (userInput.equalsIgnoreCase("Exit")) {
                    try {
                        listenerThread.join(2000);
                    } catch (InterruptedException ignored) {}
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
