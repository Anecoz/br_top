package utils;


import graphics.IndexedVertexArray;

public class GraphicsUtils {

    public static IndexedVertexArray createModelQuad() {
        float z = -0.3f;
        float[] vertices = new float[] {
                0.0f, 0.0f, z,
                0.0f, 1.0f, z,
                1.0f, 0.0f, z,
                1.0f, 1.0f, z
        };

        float[] texCoords = new float[] {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 1, 3
        };

        return new IndexedVertexArray(vertices, texCoords, indices);
    }
}
