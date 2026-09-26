package fr.fortytwo.sockets.client.app;

import fr.fortytwo.sockets.client.client.Client;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--server-port=")) {
            System.err.println("Usage: java -jar target/socket-client.jar --server-port=8081");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0].substring("--server-port=".length()));
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number.");
            return;
        }

        Client client = new Client("localhost", port);
        client.start();
    }
}
