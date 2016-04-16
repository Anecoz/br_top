package logic;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import static org.lwjgl.opengl.GL13.*;

import org.joml.Matrix4f;
import tiled.core.*;
import tiled.io.TMXMapReader;
import utils.FileUtils;
import utils.LevelUtils;

import java.awt.*;
import java.io.File;

// Holds information about the tiled level
public class Level {

    private Map map;
    private VertexArray vertexArray;
    private Shader shader;
    private Texture textureAtlas;
    private TileLayer tileLayer;

    public Level(String filename) {
        try {
            File mapFile = new File(FileUtils.RES_DIR + filename);
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

    public boolean getCollAt(int x, int y) {
        Tile tile = tileLayer.getTileAt(x, y);
        String isColl = null;
        try {
            isColl = tile.getProperties().getProperty("isCollision");
        }
        catch (Exception e) {
            System.err.println("Moved outside: " + e.getMessage());
        }

        return (isColl != null && isColl.equals("1"));
    }

    public Rectangle getBounds(){
        return map.getBounds();
    }

    public boolean InBounds(int x,int y){
        return map.inBounds(x,y);
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
        shader = new Shader("level.vert", "level.frag");
    }

    private void initMap() {
        // Setup the VAO from the map
        MapLayer layer = map.getLayer(0);

        if (layer instanceof TileLayer) {
            tileLayer = (TileLayer) layer;
            int width = tileLayer.getWidth();
            int height = tileLayer.getHeight();
            float[] tco = LevelUtils.calcTexCoords(tileLayer, width, height);
            float[] vbo = LevelUtils.calcVertices(width, height);
            vertexArray = new VertexArray(vbo, tco);
        }
    }
}
