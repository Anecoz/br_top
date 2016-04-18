package logic;

import graphics.Shader;
import graphics.shadows.ShadowTexture;
import graphics.Texture;
import graphics.VertexArray;

import static org.lwjgl.opengl.GL13.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
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
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Map loading failed!");
        }
    }

    public VertexArray getMesh() {return vertexArray;}

    public Tile getTileAt(int x, int y) {
        return tileLayer.getTileAt(x, y);
    }

    public boolean isShadowCastingTile(Tile tile) {
        String isShadow = null;
        try {
            isShadow = tile.getProperties().getProperty("isShadowCaster");
        }
        catch (Exception e) {
        }
        return (isShadow != null && isShadow.equals("1"));
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

    public void render(Matrix4f projMatrix, ShadowTexture shadowMap, Player player) {
        shader.comeHere();

        shader.uploadMatrix(projMatrix, "projMatrix");
        shader.uploadVec(new Vector2f(player.getPosition().x + player.getSize(), player.getPosition().y + player.getSize()), "lightPos");
        shader.uploadInt(getBounds().width, "worldWidth");
        shader.uploadInt(getBounds().height, "worldHeight");

        // Textures
        glActiveTexture(GL_TEXTURE0);
        textureAtlas.bind();
        glActiveTexture(GL_TEXTURE1);
        shadowMap.bind();
        vertexArray.draw();

        textureAtlas.unbind();
        shadowMap.unbind();
        glActiveTexture(GL_TEXTURE0);
        shader.pissOff();
    }

    private void initTexture() {
        TileSet tileSet = map.getTileSets().get(0);
        textureAtlas = new Texture(tileSet.getTilebmpFile());
    }

    private void initShader() {
        shader = new Shader("level.vert", "level.frag");
        shader.comeHere();
        glActiveTexture(GL_TEXTURE0);
        shader.uploadTexture(0, "atlas");
        glActiveTexture(GL_TEXTURE1);
        shader.uploadTexture(1, "shadowTex");
        shader.pissOff();
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
