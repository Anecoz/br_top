package graphics.lowlevel;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class IndexedVertexArray extends VertexArray {
    private int ibo;

    public IndexedVertexArray(float[] vertices, byte[] indices, int floatPerVertex) {
        super(vertices, floatPerVertex);

        this.count = indices.length;

        super.bind();
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        ByteBuffer indBuffer = BufferUtils.createByteBuffer(indices.length);
        indBuffer.put(indices);
        indBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL_STATIC_DRAW);
        super.unbind();
    }

    public IndexedVertexArray(float[] vertices, float[] texCoords, byte[] indices, int floatPerVertex) {
        // Base class takes care of these
        super(vertices, texCoords, floatPerVertex);

        this.count = indices.length;

        super.bind();
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        ByteBuffer indBuffer = BufferUtils.createByteBuffer(indices.length);
        indBuffer.put(indices);
        indBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL_STATIC_DRAW);
        super.unbind();
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        glDeleteBuffers(ibo);
    }

    @Override
    public void draw() {
        super.bind();
        glDrawElements(GL_TRIANGLES, this.count, GL_UNSIGNED_BYTE, 0);
        super.unbind();
    }
}
