package fr.fortytwo.printer.app;

import java.io.IOException;

import fr.fortytwo.printer.logic.ImageLoader;
import fr.fortytwo.printer.logic.CommandLineArgs;

import com.beust.jcommander.JCommander;

public class Program {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("[" + arg + "]");
        }

        final CommandLineArgs cArgs = new CommandLineArgs();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(cArgs)
                .build();

                
        jCommander.parse(args);


        try {
            final ImageLoader imageLoader = new ImageLoader();
            imageLoader.printImageAsChars(cArgs);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
