package logic;

import graphics.Camera;
import graphics.shaders.Shader;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowTexture;
import graphics.lowlevel.Texture;
import graphics.lowlevel.VertexArray;

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
    private Texture textureAtlas;
    private TileLayer tileLayer;

    public Level(String filename) {
        try {
            File mapFile = new File(FileUtils.RES_DIR + filename);
            map = new TMXMapReader().readMap(mapFile.getAbsolutePath());
            initMap();
            initTexture();
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
        ShaderHandler.levelShader.comeHere();

        ShaderHandler.levelShader.uploadMatrix(projMatrix, "projMatrix");
        ShaderHandler.levelShader.uploadVec(new Vector2f(player.getPosition().x + player.getSize(), player.getPosition().y + player.getSize()), "lightPos");
        ShaderHandler.levelShader.uploadInt(getBounds().width, "worldWidth");
        ShaderHandler.levelShader.uploadInt(getBounds().height, "worldHeight");
        ShaderHandler.levelShader.uploadInt((int)Camera.WIN_SIZE_X, "windowSize");

        // Textures
        glActiveTexture(GL_TEXTURE0);
        textureAtlas.bind();
        glActiveTexture(GL_TEXTURE1);
        shadowMap.bind();
        vertexArray.draw();

        textureAtlas.unbind();
        shadowMap.unbind();
        glActiveTexture(GL_TEXTURE0);
        ShaderHandler.levelShader.pissOff();
    }

    private void initTexture() {
        TileSet tileSet = map.getTileSets().get(0);
        textureAtlas = new Texture(tileSet.getTilebmpFile());
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
