package raytracer.render;

import raytracer.math.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


 // A grid of colours (the render target) that can also save itself. I used PNG via the ImageIO, and PPM which is written by hand (a tiny header plus raw RGB bytes) that i have in the book  in the spirit of from scratch. Colors are gamma-corrected on the way out so the image is not too dark on a display.

public final class Image {
// i am not creating an image here .image is in render because it's related to producing the final rendered image.
    private final int width; // how many pixels att x
    private final int height;// how many pixels at y
    private final Color[] pixels;// to store the actual color as a long array, as each position contains a color it wasn't smart to do it as an 2D array

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Color[width * height]; //this is the mapping
    }

    public void setPixel(int x, int y, Color color) {
        pixels[y * width + x] = color;
    }

    public void savePng(Path path) throws IOException {
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = pixels[y * width + x];
                buffer.setRGB(x, y, (toByte(c.r()) << 16) | (toByte(c.g()) << 8) | toByte(c.b()));
            }
        }
        ImageIO.write(buffer, "png", path.toFile());
    }

    public void savePpm(Path path) throws IOException {
        String header = "P6\n" + width + " " + height + "\n255\n";
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path))) { // open the file
            out.write(header.getBytes(StandardCharsets.US_ASCII));
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color c = pixels[y * width + x];
                    out.write(toByte(c.r()));
                    out.write(toByte(c.g()));
                    out.write(toByte(c.b()));
                }
            }
        }
    }

    //Linear color component -> 0..255 byte, with gamma 2.0 correction.
    private static int toByte(double component) {
        double gamma = Math.sqrt(Math.clamp(component, 0.0, 1.0));
        return (int) (255.999 * gamma);
    }
}
//IMAGE
//
//I have:
//    width
//    height
//    array of pixel colors
//
//
//WHEN SOMEONE CREATES ME:
//
//    remember width
//    remember height
//
//    create enough storage for:
//        width × height pixels
//
//
//WHEN RENDERER SAYS setPixel(x, y, color):
//
//    convert x,y into an array position
//
//    store the color there
//
//
//WHEN SOMEONE SAYS savePng():
//
//    create a Java BufferedImage
//
//    for every pixel:
//        get its color
//        convert R, G, B from 0–1 into 0–255
//        put RGB into BufferedImage
//
//    save BufferedImage as PNG
//
//
//WHEN SOMEONE SAYS savePpm():
//
//    create a PPM file
//
//    write PPM header
//
//    for every pixel:
//        write red
//        write green
//        write blue
//
//
//WHEN CONVERTING A COLOR TO A BYTE:
//
//    make sure value is between 0 and 1
//
//    apply gamma correction
//
//    convert 0–1 into 0–255