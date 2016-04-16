package utils;

import java.util.ArrayList;

public class LevelUtils {

    public static float[] floatListToArray(ArrayList<Float> listIn) {
        float[] output = new float[listIn.size()];
        int i = 0;
        for (Float f : listIn) {
            output[i++] = (f != null ? f : Float.NaN);
        }

        return output;
    }

    public static float[] calcTexCoords(int width, int height) {
        float[] tco = new float[(width + 1) * (height + 1) * 2];

        // Loop through tiles and set normalized texture coordinates for each point
        for (int y = 0; y < (height + 1); y++) {
            for (int x = 0; x < (width + 1); x++) {
                tco[(y * (width + 1) + x) * 2 + 0] = (float) x / (float) width;
                tco[(y * (width + 1) + x) * 2 + 1] = (float) y / (float) height;
            }
        }

        return tco;
    }

    public static byte[] calcIndices(int width, int height) {
        byte[] ibo = new byte[6 * width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // First triangle
                ibo[(y * width + x) * 6 + 0] = (byte)(x + (width + 1) * (y + 1));
                ibo[(y * width + x) * 6 + 1] = (byte)(x + (width + 1) * y);
                ibo[(y * width + x) * 6 + 2] = (byte)((x + 1) + (width + 1) * (y + 1));

                // Second triangle
                ibo[(y * width + x) * 6 + 3] = (byte)((x + 1) + (width + 1) * (y + 1));
                ibo[(y * width + x) * 6 + 4] = (byte)(x + (width + 1) * y);
                ibo[(y * width + x) * 6 + 5] = (byte)((x + 1) + (width + 1) * y);
            }
        }

        return ibo;
    }

    public static float[] calcVertices(int width, int height) {
        float[] vbo = new float[(width + 1) * (height + 1) * 3];

        float z = 0.0f; // Use the same depth value for every tile

        return vbo;
    }

}
