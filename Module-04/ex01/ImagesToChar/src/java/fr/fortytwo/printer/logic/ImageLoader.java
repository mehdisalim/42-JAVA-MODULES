package fr.fortytwo.printer.logic;


import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageLoader {
    private final BufferedImage image;

    public ImageLoader() throws IOException {
        final InputStream input = ImageLoader.class.getResourceAsStream("/image.bmp");
        this.image = ImageIO.read(input);
    }

    public void printImageAsChars(final char whiteColorChar, final char blackColorChar) {
        for (int h = 0; h < this.image.getHeight(); h++) {
            for (int w = 0; w < this.image.getWidth(); w++) {
                final int rgbColor = this.image.getRGB(w, h);
                if (rgbColor == Color.BLACK.getRGB()) {
                    System.out.print(blackColorChar);
                } else {
                    System.out.print(whiteColorChar);
                }
            }
            System.out.println();
        }
    }

}