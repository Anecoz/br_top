package graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import utils.ShaderUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    // Make these ints so that we can create general VAOs without a loaded specific shader program
    public static final int VERTEX_ATTRIB = 0;
    public static final int TEX_ATTRIB = 1;
    private static final String SHADERS_DIR = "src/shaders/";

    private int id;

    public int getId() {return id;}

    public Shader(String vertex, String frag) {
        id = ShaderUtils.load(SHADERS_DIR + vertex, SHADERS_DIR + frag);

        if (id == -1)
            System.err.println("Shaders failed to load!");
    }

    public void uploadMatrix(Matrix4f mat, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        mat.get(fb);
        glUniformMatrix4fv(loc, false, fb);
    }

    public void uploadTexture(int texUnit, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        glUniform1i(loc, texUnit);
    }

    public void comeHere() {
        glUseProgram(id);
    }

    public void pissOff() {
        glUseProgram(0);
    }
}
