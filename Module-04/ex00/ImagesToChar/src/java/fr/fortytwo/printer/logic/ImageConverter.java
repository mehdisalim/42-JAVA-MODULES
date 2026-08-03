package fr.fortytwo.printer.logic;

import java.io.File;
import java.io.IOException;

import fr.fortytwo.printer.logic.BmpPixelParser.BmpImage;


public class ImageConverter {
    private final BmpImage image;

    public ImageConverter(final String imagePath) throws IOException {
        this.image = BmpPixelParser.parse(new File(imagePath));
    }

    public void printImageAsChars(final char whiteColorChar, final char blackColorChar) {
        for (int h = 0; h < this.image.getHeight(); h++) {
            for (int w = 0; w < this.image.getWidth(); w++) {
                final int rgbColor = this.image.getRGB(w, h);
                if (rgbColor != -1) {
                    System.out.print(blackColorChar);
                } else {
                    System.out.print(whiteColorChar);
                }
            }
            System.out.println();
        }
    }

}