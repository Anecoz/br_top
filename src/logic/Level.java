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

            // Create a float array from the arraylist
            float[] tco = LevelUtils.calcTexCoords(tileLayer, width, height);
            float[] vbo = LevelUtils.calcVertices(width, height);

            // Create the VAO
            vertexArray = new VertexArray(vbo, tco);
        }
    }
}
