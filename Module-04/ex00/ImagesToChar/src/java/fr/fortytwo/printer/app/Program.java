package fr.fortytwo.printer.app;

import java.io.IOException;

import fr.fortytwo.printer.logic.ImageConverter;

public class Program {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Invalid Arguments");
            System.exit(1);
        }

        final char whiteCharacter = args[0].charAt(0);
        final char blackCharacter = args[1].charAt(0);
        final String imagePath = args[2];

        try {
            final ImageConverter imageConverter = new ImageConverter(imagePath);
            imageConverter.printImageAsChars(whiteCharacter, blackCharacter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
