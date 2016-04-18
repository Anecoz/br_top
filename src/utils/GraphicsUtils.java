package utils;


import graphics.lowlevel.IndexedVertexArray;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class GraphicsUtils {

    public static void printError(String message) {
        int error;
        if ((error = glGetError()) != GL_NO_ERROR) {
            System.out.println("Error: " + message + ", code: " + error);
        }
    }

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
