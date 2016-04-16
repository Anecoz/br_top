package graphics;

import org.lwjgl.BufferUtils;
import tiled.core.Map;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexArray {
    private int vao, vbo, tbo;
    private int count;

    public VertexArray(float[] vertices, float[] texCoords) {
        count = vertices.length/3;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertBuffer.put(vertices);
        vertBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        FloatBuffer texBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        texBuffer.put(texCoords);
        texBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.TEX_ATTRIB, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TEX_ATTRIB);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(tbo);
    }

    private void bind() {
        glBindVertexArray(vao);
    }

    private void unbind() {
        glBindVertexArray(0);
    }

    public void draw() {
        bind();
        glDrawArrays(GL_TRIANGLES, 0, count);
        unbind();
    }
}
