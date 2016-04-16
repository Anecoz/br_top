package logic;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import static org.lwjgl.opengl.GL13.*;

import org.joml.Matrix4f;
import tiled.core.*;
import tiled.io.TMXMapReader;
import utils.LevelUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// Holds information about the tiled level
public class Level {

    private Map map;
    private VertexArray vertexArray;
    private static final String RES_DIR = "res/";
    private Shader shader;
    private Texture textureAtlas;

    public Level(String filename) {
        try {
            File mapFile = new File(RES_DIR + filename);
            map = new TMXMapReader().readMap(mapFile.getAbsolutePath());
            initMap();
            initTexture();
            initShader();

            glActiveTexture(GL_TEXTURE0);
            shader.uploadTexture(0, "atlas");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Map loading failed!");
        }
    }

    public void render(Matrix4f projMatrix) {
        shader.comeHere();

        shader.uploadMatrix(projMatrix, "projMatrix");
        textureAtlas.bind();
        vertexArray.draw();

        textureAtlas.unbind();
        shader.pissOff();
    }

    private void initTexture() {
        TileSet tileSet = map.getTileSets().get(0);
        textureAtlas = new Texture(tileSet.getTilebmpFile());
    }

    private void initShader() {
        shader = new Shader("src/shaders/level.vert", "src/shaders/level.frag");
    }

    private void initMap() {
        // Setup the VAO from the map
        MapLayer layer = map.getLayer(0);

        if (layer instanceof TileLayer) {
            TileLayer tileLayer = (TileLayer) layer;

            // Create VAO
            int width = tileLayer.getWidth();
            int height = tileLayer.getHeight();

            ArrayList<Float> texCoordsList = new ArrayList<>();
            ArrayList<Float> verticesList = new ArrayList<>();

            // Loop over tiles to get texture coordinates (within atlas) and create vertices
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++) {
                    Tile tile = tileLayer.getTileAt(x, y);

                    // Texture coordinates are stored on the tile objects
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

                    // Vertices
                    float z = 0.0f;
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
            float[] tco = LevelUtils.floatListToArray(texCoordsList);
            float[] vbo = LevelUtils.floatListToArray(verticesList);

            // Create the VAO
            vertexArray = new VertexArray(vbo, tco);
        }
    }
}
