package graphics.lowlevel;

import graphics.shaders.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexArray {
    protected int vao, vbo, tbo;
    protected int count;

    public VertexArray(float[] vertices, float[] texCoords, int floatPerVertex) {
        count = vertices.length/3;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertBuffer.put(vertices);
        vertBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertBuffer, GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(Shader.VERTEX_ATTRIB, floatPerVertex, GL_FLOAT, false, 0, 0);
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

    public int getVAOID() {return vao;}
    public int getCount() {return count;}

    protected void bind() {
        glBindVertexArray(vao);
    }

    protected void unbind() {
        glBindVertexArray(0);
    }

    public void draw() {
        bind();
        glDrawArrays(GL_TRIANGLES, 0, count);
        unbind();
    }

    public void drawPoints() {
        bind();
        glDrawArrays(GL_POINTS, 0, count);
        unbind();
    }
}
