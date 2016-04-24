package graphics.shadows;

import utils.BufferHelpers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

// Texture that holds tile information in RGB components
// R: world x coordinate
// G: world y coordinate
// B: isShadowCaster
// A: not used
public class ShadowCasterTexture {
    private int[] data;
    private int texId;
    private int width;
    private int height;

    public ShadowCasterTexture() {

    }

    public void init(int width, int height) {
        this.width = width;
        this.height = height;
        data = new int[width*height];
    }

    public void addAt(int tx, int ty, int isShadowCaster) {
        data[ty*width + tx] = isShadowCaster;
    }

    public void convertToTexture() {
        texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferHelpers.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        // DEBUG (keep it for later
        /*int[] pixels = new int[width*height];
        IntBuffer back = ByteBuffer.allocateDirect(pixels.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        glBindTexture(GL_TEXTURE_2D, texId);
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, back);
        back.get(pixels);
        System.out.println("data arr was: " + Arrays.toString(data));
        System.out.println("Texture back is: " + Arrays.toString(pixels));*/
    }

    public void cleanUp() {
        glDeleteTextures(texId);
    }
    public void bind() {glBindTexture(GL_TEXTURE_2D, texId);}
    public void unbind() {glBindTexture(GL_TEXTURE_2D, 0);}
}
