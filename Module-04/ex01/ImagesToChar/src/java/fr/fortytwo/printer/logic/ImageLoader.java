package fr.fortytwo.printer.logic;

import java.io.IOException;
import java.io.InputStream;

import fr.fortytwo.printer.logic.BmpPixelParser.BmpImage;


public class ImageLoader {
    private final BmpImage image;

    public ImageLoader() throws IOException {
        final InputStream input = ImageLoader.class.getResourceAsStream("/resources/image.bmp");
        this.image = BmpPixelParser.parse(input);
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