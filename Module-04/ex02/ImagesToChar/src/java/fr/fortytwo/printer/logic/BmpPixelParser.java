package fr.fortytwo.printer.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BmpPixelParser {

    public static class BmpImage {
        private int width;
        private int height;
        private int bitsPerPixel;
        private Pixel[][] pixels; // pixels[y][x]

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getBitsPerPixel() {
            return bitsPerPixel;
        }

        public int getRGB(int x, int y) {
            return pixels[y][x].toRGB();
        }

        public Pixel getPixel(int x, int y) {
            return pixels[y][x];
        }
    }

    public static class Pixel {
        public final int red;
        public final int green;
        public final int blue;
        public final int alpha;

        public Pixel(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public int toRGB() {
            return ((alpha & 0xFF) << 24) |
                   ((red   & 0xFF) << 16) |
                   ((green & 0xFF) << 8)  |
                   (blue   & 0xFF);
        }

        @Override
        public String toString() {
            return String.format("RGBA(%d, %d, %d, %d)", red, green, blue, alpha);
        }
    }

    public static BmpImage parse(File file) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            return parse(in);
        }
    }

    public static BmpImage parse(InputStream in) throws IOException {
        // 1. Read BMP File Header (14 bytes)
        int magic1 = in.read();
        int magic2 = in.read();
        if (magic1 != 'B' || magic2 != 'M') {
            throw new IllegalArgumentException("Not a valid BMP file (missing 'BM' signature)");
        }

        int fileSize = readLittleEndianInt(in);
        readLittleEndianShort(in); // Reserved 1
        readLittleEndianShort(in); // Reserved 2
        int dataOffset = readLittleEndianInt(in);

        // 2. Read DIB Header fields
        int headerSize = readLittleEndianInt(in);
        int width = readLittleEndianInt(in);
        int height = readLittleEndianInt(in);
        int planes = readLittleEndianShort(in);
        int bitsPerPixel = readLittleEndianShort(in);
        int compression = readLittleEndianInt(in);

        if (compression != 0) {
            throw new UnsupportedOperationException("Only uncompressed (BI_RGB) BMP files are supported.");
        }
        if (bitsPerPixel != 1 && bitsPerPixel != 24 && bitsPerPixel != 32) {
            throw new UnsupportedOperationException("Only 1-bit, 24-bit, and 32-bit BMP files are supported. Found: " + bitsPerPixel);
        }

        readLittleEndianInt(in); // imageSize
        readLittleEndianInt(in); // xPpm
        readLittleEndianInt(in); // yPpm
        int colorsUsed = readLittleEndianInt(in);
        readLittleEndianInt(in); // colorsImportant

        int headerBytesRead = 40;
        while (headerBytesRead < headerSize) {
            in.read();
            headerBytesRead++;
        }

        int totalBytesRead = 14 + headerBytesRead;

        // 3. Read Palette (Color Table) if indexed color depth (e.g. 1-bit)
        Pixel[] palette = null;
        if (bitsPerPixel <= 8) {
            int numColors = (colorsUsed == 0) ? (1 << bitsPerPixel) : colorsUsed;
            palette = new Pixel[numColors];
            for (int i = 0; i < numColors; i++) {
                int b = in.read();
                int g = in.read();
                int r = in.read();
                in.read(); // Reserved byte
                totalBytesRead += 4;
                palette[i] = new Pixel(r, g, b, 255);
            }
        }

        // Skip extra bytes up to pixel data offset
        while (totalBytesRead < dataOffset) {
            in.read();
            totalBytesRead++;
        }

        // 4. Setup dimensions and orientation
        boolean isBottomUp = (height > 0);
        int absHeight = Math.abs(height);

        BmpImage bmp = new BmpImage();
        bmp.width = width;
        bmp.height = absHeight;
        bmp.bitsPerPixel = bitsPerPixel;
        bmp.pixels = new Pixel[absHeight][width];

        // 5. Read Pixels
        if (bitsPerPixel == 1) {
            int rowBytes = (width + 7) / 8;
            int paddingBytes = (4 - (rowBytes % 4)) % 4;

            for (int row = 0; row < absHeight; row++) {
                int targetY = isBottomUp ? (absHeight - 1 - row) : row;
                int currentByte = 0;

                for (int x = 0; x < width; x++) {
                    if (x % 8 == 0) {
                        currentByte = in.read();
                        if (currentByte == -1) {
                            throw new IOException("Unexpected EOF while reading 1-bit pixels");
                        }
                    }
                    int bitIndex = 7 - (x % 8);
                    int paletteIndex = (currentByte >> bitIndex) & 1;
                    bmp.pixels[targetY][x] = palette[paletteIndex];
                }

                for (int p = 0; p < paddingBytes; p++) {
                    in.read();
                }
            }
        } else {
            // 24-bit or 32-bit BMP
            int bytesPerPixel = bitsPerPixel / 8;
            int rowBytes = width * bytesPerPixel;
            int paddingBytes = (4 - (rowBytes % 4)) % 4;

            for (int row = 0; row < absHeight; row++) {
                int targetY = isBottomUp ? (absHeight - 1 - row) : row;

                for (int x = 0; x < width; x++) {
                    int blue = in.read();
                    int green = in.read();
                    int red = in.read();
                    int alpha = (bitsPerPixel == 32) ? in.read() : 255;

                    if (blue == -1 || green == -1 || red == -1) {
                        throw new IOException("Unexpected EOF while reading pixels");
                    }

                    bmp.pixels[targetY][x] = new Pixel(red, green, blue, alpha);
                }

                for (int p = 0; p < paddingBytes; p++) {
                    in.read();
                }
            }
        }

        return bmp;
    }

    private static int readLittleEndianShort(InputStream in) throws IOException {
        int b1 = in.read();
        int b2 = in.read();
        if ((b1 | b2) < 0) throw new IOException("Unexpected end of stream");
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8);
    }

    private static int readLittleEndianInt(InputStream in) throws IOException {
        int b1 = in.read();
        int b2 = in.read();
        int b3 = in.read();
        int b4 = in.read();
        if ((b1 | b2 | b3 | b4) < 0) throw new IOException("Unexpected end of stream");
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 24);
    }
}