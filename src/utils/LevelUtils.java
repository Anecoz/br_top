package utils;

import tiled.core.Tile;
import tiled.core.TileLayer;

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

    public static float[] calcTexCoords(TileLayer layer, int width, int height) {

        ArrayList<Float> texCoordsList = new ArrayList<>();

        // Loop over tiles to get texture coordinates (within atlas) and create vertices
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                Tile tile = layer.getTileAt(x, y);
                float[] texCoords = tile.getTexCoords();
                // First triangle
                texCoordsList.add(texCoords[0]);
                texCoordsList.add(texCoords[1]);

                texCoordsList.add(texCoords[2]);
                texCoordsList.add(texCoords[3]);

                texCoordsList.add(texCoords[4]);
                texCoordsList.add(texCoords[5]);
                // Second triangle
                texCoordsList.add(texCoords[4]);
                texCoordsList.add(texCoords[5]);

                texCoordsList.add(texCoords[2]);
                texCoordsList.add(texCoords[3]);

                texCoordsList.add(texCoords[6]);
                texCoordsList.add(texCoords[7]);
            }

        return LevelUtils.floatListToArray(texCoordsList);
    }

    public static float[] calcVertices(int width, int height) {
        ArrayList<Float> verticesList = new ArrayList<>();
        float z = 0.0f;

        // Loop over tiles to get texture coordinates (within atlas) and create vertices
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                // First triangle
                verticesList.add((float) x);
                verticesList.add((float) y);
                verticesList.add(z);

                verticesList.add((float) x);
                verticesList.add((float) (y + 1));
                verticesList.add(z);

                verticesList.add((float) (x + 1));
                verticesList.add((float) y);
                verticesList.add(z);
                // Second triangle
                verticesList.add((float) (x + 1));
                verticesList.add((float) y);
                verticesList.add(z);

                verticesList.add((float) x);
                verticesList.add((float) (y + 1));
                verticesList.add(z);

                verticesList.add((float) (x + 1));
                verticesList.add((float) (y + 1));
                verticesList.add(z);
            }

        // Create a float array from the arraylist
        return LevelUtils.floatListToArray(verticesList);
    }

}
