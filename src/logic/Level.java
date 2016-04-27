package logic;

import graphics.Camera;
import graphics.lighting.LightHandler;
import graphics.shaders.ShaderHandler;
import graphics.lowlevel.Texture;
import graphics.lowlevel.VertexArray;

import static org.lwjgl.opengl.GL13.*;

import graphics.shadows.ShadowHandler;
import logic.inventory.InventoryItem;
import logic.weapons.Pistol;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector2i;
import tiled.core.*;
import tiled.io.TMXMapReader;
import utils.FileUtils;
import utils.LevelUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Holds information about the tiled level
public class Level {

    private Map map;
    private VertexArray vertexArray;
    private Texture textureAtlas;
    private TileLayer tileLayer;
    private HashMap<Vector2i, List<InventoryItem>> droppedItems;    // Items that lay out on the level
    private static CopyOnWriteArrayList<InventoryItem> itemsToBeAdded; // Used to let network thread add new items to level
    private static ConcurrentHashMap<Integer, Vector2i> itemsToBeRemoved; // Network thread adds items in here

    // Some optimization vectors
    private Vector2f playerPosUniform = new Vector2f(0);

    public Level(String filename) {
        try {
            File mapFile = new File(FileUtils.RES_DIR + filename);
            map = new TMXMapReader().readMap(mapFile.getAbsolutePath());
            droppedItems = new HashMap<>();
            itemsToBeAdded = new CopyOnWriteArrayList<>();
            itemsToBeRemoved = new ConcurrentHashMap<>();

            initMap();
            initTexture();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Map loading failed!");
        }
    }

    public synchronized void update() {
        // Check if there are items to be added to the level
        if (itemsToBeAdded.size() > 0) {
            for (InventoryItem item : itemsToBeAdded) {
                addDroppedItem(item);
            }
            itemsToBeAdded.clear();
        }
        // or any items to be removed from the level
        if (itemsToBeRemoved.size() > 0) {
            for (ConcurrentHashMap.Entry<Integer, Vector2i> entry : itemsToBeRemoved.entrySet()) {
                removeItemById(entry.getValue(), entry.getKey());
            }
            itemsToBeRemoved.clear();
        }
    }

    public synchronized static void addItemToRemoveToQueue(Vector2i position, int uniqueId) {
        itemsToBeRemoved.put(uniqueId, position);
    }

    public synchronized static void addPickupToQueue(InventoryItem item) {
        itemsToBeAdded.add(item);
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

    public InventoryItem getClosestItemAt(Vector2f playerPos) {
        // Loop 9 closest
        InventoryItem item = null;
        float minDistance = 10000.0f;
        //Vector2i playerI = new Vector2i((int)playerPos.x, (int)playerPos.y);
        int playerIx = (int)playerPos.x;
        int playerIy = (int)playerPos.y;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (droppedItems.containsKey(new Vector2i(playerIx + x, playerIy + y))) {
                    List<InventoryItem> list = droppedItems.get(new Vector2i(playerIx + x, playerIy + y));
                    for (InventoryItem currItem : list) {
                        if (playerPos.distance(currItem.getPosition()) < minDistance) {
                            item = currItem;
                            minDistance = playerPos.distance(currItem.getPosition());
                        }
                    }
                }
            }
        }

        return item;
    }

    private void removeItemById(Vector2i position, int uniqueId) {
        InventoryItem toRemove = null;
        if (droppedItems.containsKey(position)) {
            List<InventoryItem> list = droppedItems.get(position);
            for (InventoryItem item : list) {
                if (item.getUniqueId() == uniqueId) {
                    toRemove = item;
                    break;
                }
            }
            if (toRemove != null) {
                toRemove.cleanUp();
                list.remove(toRemove);
                if (list.size() == 0)
                    droppedItems.remove(position);
            }
        }
    }

    public InventoryItem getDroppedItemById(Vector2i position, int uniqueId) {
        InventoryItem out = null;
        if (droppedItems.containsKey(position)) {
            // Don't forget to remove from the list
            List<InventoryItem> list = droppedItems.get(position);
            for (InventoryItem item : list) {
                if (item.getUniqueId() == uniqueId) {
                    out = item;
                    break;
                }
            }
            list.remove(out);
            if (list.size() == 0)
                droppedItems.remove(position);
        }
        return out;
    }

    public void addDroppedItem(InventoryItem item) {
        // First check if there already is an item at that position
        Vector2i position = new Vector2i((int)item.getPosition().x, (int)item.getPosition().y);
        if (!droppedItems.containsKey(position))
            droppedItems.put(position, new ArrayList<>());

        droppedItems.get(position).add(item);
    }

    public void render(Matrix4f projMatrix, Player player) {
        ShaderHandler.levelShader.comeHere();
        glDisable(GL_MULTISAMPLE);

        playerPosUniform.x = player.getPosition().x + player.getWidth()/2.0f;
        playerPosUniform.y = player.getPosition().y + player.getHeight()/2.0f;
        ShaderHandler.levelShader.uploadMatrix(projMatrix, "projMatrix");
        ShaderHandler.levelShader.uploadVec(playerPosUniform, "playerPos");
        ShaderHandler.levelShader.uploadInt(getBounds().width, "worldWidth");
        ShaderHandler.levelShader.uploadInt(getBounds().height, "worldHeight");
        ShaderHandler.levelShader.uploadFloat(Camera.getWinSizeX(), "windowSizeX");
        ShaderHandler.levelShader.uploadFloat(Camera.getWinSizeY(), "windowSizeY");
        ShaderHandler.levelShader.uploadInt(1, "numLights");
        ShaderHandler.levelShader.uploadVec(LightHandler.lightList.get(0), "lightPos");
        ShaderHandler.levelShader.uploadVec(Camera.getPosition(), "camPos");

        // Textures
        glActiveTexture(GL_TEXTURE0);
        textureAtlas.bind();
        glActiveTexture(GL_TEXTURE1);
        ShadowHandler.bindShadowMap();
        vertexArray.draw();

        textureAtlas.unbind();
        ShadowHandler.unbindShadowMap();
        glActiveTexture(GL_TEXTURE0);
        ShaderHandler.levelShader.pissOff();
        glEnable(GL_MULTISAMPLE);

        renderDroppedItems(projMatrix);
    }

    private void renderDroppedItems(Matrix4f projection) {
        for (HashMap.Entry entry : droppedItems.entrySet()) {
            List<InventoryItem> list = (List<InventoryItem>)entry.getValue();
            for (InventoryItem item : list) {
                item.renderDisplay(projection);
            }
        }
    }

    private void initTexture() {
        TileSet tileSet = map.getTileSets().get(0);
        textureAtlas = new Texture(tileSet.getTilebmpFile(), 1.0f);
    }

    private void initMap() {
        // Setup the VAO from the map
        MapLayer layer = map.getLayer(0);

        if (layer instanceof TileLayer) {
            tileLayer = (TileLayer) layer;
            int width = tileLayer.getWidth();
            int height = tileLayer.getHeight();
            float[] tco = LevelUtils.calcTexCoords(tileLayer, width, height);
            float[] vbo = LevelUtils.calcVertices(width, height, 0.0f);
            vertexArray = new VertexArray(vbo, tco, 3);
        }
    }

    public void cleanUp() {
        for (HashMap.Entry entry : droppedItems.entrySet()) {
            List<InventoryItem> list = (List<InventoryItem>)entry.getValue();
            for (InventoryItem item : list) {
                item.cleanUp();
            }
            list.clear();
        }
        droppedItems.clear();
        itemsToBeRemoved.clear();
    }
}
