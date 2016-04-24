package graphics.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import utils.ShaderUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    // Make these ints so that we can create general VAOs without a loaded specific shader program
    public static final int VERTEX_ATTRIB = 0;
    public static final int TEX_ATTRIB = 1;
    private static final String SHADERS_DIR = "src/shaders/";

    private int id, vertId, fragId;

    public int getId() {return id;}

    public Shader(String vertex, String frag) {
        List<Integer> ids = ShaderUtils.load(SHADERS_DIR + vertex, SHADERS_DIR + frag);
        id = ids.get(0);
        vertId = ids.get(1);
        fragId = ids.get(2);
        if (id == -1)
            System.err.println("Shaders failed to load!");
    }

    public void uploadFloat(float val, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        glUniform1f(loc, val);
    }

    public void uploadVec(Vector2f vec, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        FloatBuffer fb = BufferUtils.createFloatBuffer(2);
        vec.get(fb);
        glUniform2fv(loc, fb);
    }

    public void uploadVec(Vector3f vec, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        FloatBuffer fb = BufferUtils.createFloatBuffer(3);
        vec.get(fb);
        glUniform3fv(loc, fb);
    }

    public void uploadVecArr(List<Vector2f> arr, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        FloatBuffer fb = BufferUtils.createFloatBuffer(2 * arr.size());
        for (Vector2f vec : arr) {
            fb.put(vec.x);
            fb.put(vec.y);
        }
        glUniform2fv(loc, fb);
        //GL20.glUniform2fv(loc, arr.size(), fb);
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

    public void uploadInt(int val, String uniformName) {
        int loc = glGetUniformLocation(id, uniformName);
        glUniform1i(loc, val);
    }

    public void cleanUp() {
        glDetachShader(id, vertId);
        glDetachShader(id, fragId);
        glDeleteShader(vertId);
        glDeleteShader(fragId);
        glDeleteProgram(id);
    }

    public void comeHere() {
        glUseProgram(id);
    }

    public void pissOff() {
        glUseProgram(0);
    }
}
