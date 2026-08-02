package fr.fortytwo.printer.app;

import java.io.IOException;

import fr.fortytwo.printer.logic.ImageLoader;

public class Program {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid Arguments");
            System.exit(1);
        }

        final char whiteCharacter = args[0].charAt(0);
        final char blackCharacter = args[1].charAt(0);

        try {
            final ImageLoader imageLoader = new ImageLoader();
            imageLoader.printImageAsChars(whiteCharacter, blackCharacter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
