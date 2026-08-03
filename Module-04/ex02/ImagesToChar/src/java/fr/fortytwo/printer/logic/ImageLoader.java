package fr.fortytwo.printer.logic;


import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.ColoredPrinter.Builder;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageLoader {
    private final BufferedImage image;

    public ImageLoader() throws IOException {
        final InputStream input = ImageLoader.class.getResourceAsStream("/image.bmp");
        this.image = ImageIO.read(input);
    }

    public void printImageAsChars(final CommandLineArgs args) {
        final ColoredPrinter cp = new ColoredPrinter(new Builder(0, false));
        for (int h = 0; h < this.image.getHeight(); h++) {
            for (int w = 0; w < this.image.getWidth(); w++) {
                final int rgbColor = this.image.getRGB(w, h);
                if (rgbColor == Color.BLACK.getRGB()) {
                    cp.print(args.getBlack());
                } else {
                    cp.print(args.getWhite());
                }
            }
            cp.println("\n");
        }
    }

}