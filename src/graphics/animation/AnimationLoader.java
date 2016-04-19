package graphics.animation;

import graphics.lowlevel.Texture;
import tiled.util.BasicTileCutter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Uses BasicTileCutter from Tiled lib to get an array of textures to be used for animations
public class AnimationLoader {
    private static BasicTileCutter tileCutter;

    public static List<Texture> loadTextures(String path, int width, int height, int spacing, int margin)
    throws IOException {
        // Setup the tile cutter
        tileCutter = new BasicTileCutter(width, height, spacing, margin);

        // Load the image
        BufferedImage image = ImageIO.read(new FileInputStream(path));
        if (image == null) {
            throw new IOException("Failed to load " + path);
        }

        tileCutter.setImage(image);

        // Loop through all images and load textures
        List<Texture> output = new ArrayList<>();
        Image currImage = tileCutter.getNextTile();
        while (currImage != null) {
            output.add(new Texture(toBufferedImage(currImage)));
            currImage = tileCutter.getNextTile();
        }

        return output;
    }

    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
