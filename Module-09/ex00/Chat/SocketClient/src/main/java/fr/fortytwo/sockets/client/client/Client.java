package fr.fortytwo.sockets.client.client;

import java.io.*;
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

            while (true) {
                String serverMessage = reader.readLine();
                if (serverMessage == null) break;
                
                System.out.println(serverMessage);
                
                if (serverMessage.equals("Successful!") || serverMessage.startsWith("Failed:")) {
                    break;
                }
                
                System.out.print("> ");
                String userInput = scanner.nextLine();
                writer.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
