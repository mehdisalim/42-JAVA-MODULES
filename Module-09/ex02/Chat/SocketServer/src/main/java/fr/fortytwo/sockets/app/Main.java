package fr.fortytwo.sockets.app;

import fr.fortytwo.sockets.config.SocketsApplicationConfig;
import fr.fortytwo.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--port=")) {
            System.err.println("Usage: java -jar target/socket-server.jar --port=8081");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0].substring("--port=".length()));
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number.");
            return;
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Server server = context.getBean(Server.class);
        server.start(port);
    }
}
