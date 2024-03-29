package graphics.lowlevel;

import utils.BufferHelpers;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int width, height;
    private float widthAfterScale;
    private float heightAfterScale;
    private float scale = 1.0f;
    private int texture;
    private int type = GL_TEXTURE_2D;

    public Texture(int width, int height, int id, int type) {
        this.width = width;
        this.height = height;
        this.texture = id;
        this.type = type;
    }

    public Texture(String path, float scale) {
        this.scale = scale;
        texture = load(path);
        setSizes();
    }

    public Texture(String path) {
        texture = load(path);
        this.scale = 1.0f;
    }

    public Texture(BufferedImage image) {
        texture = load(image);
        setSizes();
    }

    private void setSizes() {
        if (width > 1.0 || height > 1.0) {
            if (width >= height) {
                heightAfterScale = height / width;
                widthAfterScale = 1.0f;
            }
            else {
                widthAfterScale = width/height;
                heightAfterScale = 1.0f;
            }
        }

        widthAfterScale *= scale;
        heightAfterScale *= scale;
    }

    public float getWidthAfterScale() {return widthAfterScale;}

    public float getHeightAfterScale() {return heightAfterScale;}

    public int getID() {return texture;}

    public float getScale() {return scale;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    private int load(BufferedImage image) {
        int[] pixels = null;
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferHelpers.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }

    private int load(String path) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferHelpers.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }

    public void cleanUp() {
        glDeleteTextures(texture);
    }

    public void bind() {
        glBindTexture(type, texture);
    }

    public void unbind() {
        glBindTexture(type, 0);
    }
}
