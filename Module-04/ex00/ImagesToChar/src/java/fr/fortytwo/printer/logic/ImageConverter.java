package fr.fortytwo.printer.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageConverter {
    private final BufferedImage image;

    public ImageConverter(final String imagePath) throws IOException {
        this.image = ImageIO.read(new File(imagePath));
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