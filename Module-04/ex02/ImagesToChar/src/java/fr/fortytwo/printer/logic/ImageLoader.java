package fr.fortytwo.printer.logic;


import java.io.IOException;
import java.io.InputStream;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.ColoredPrinter.Builder;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

import fr.fortytwo.printer.logic.BmpPixelParser.BmpImage;



public class ImageLoader {
    private final BmpImage image;

    public ImageLoader() throws IOException {
        final InputStream in = ImageLoader.class.getResourceAsStream("/resources/image.bmp");
        this.image = BmpPixelParser.parse(in);
    }

    public void printImageAsChars(final CommandLineArgs args) {
        final ColoredPrinter cp = new ColoredPrinter(new Builder(1, false));
        final BColor whiteColor = BColor.valueOf(args.getWhite().toUpperCase());
        final BColor blackColor = BColor.valueOf(args.getBlack().toUpperCase());

        for (int h = 0; h < this.image.getHeight(); h++) {
            for (int w = 0; w < this.image.getWidth(); w++) {
                final int rgbColor = this.image.getRGB(w, h);
                if (rgbColor == -1) {
                    cp.print(" ", Attribute.NONE, FColor.NONE, whiteColor);
                } else {
                    cp.print(" ", Attribute.NONE, FColor.NONE, blackColor);
                }
            }
            cp.println(" ");
        }
    }

}