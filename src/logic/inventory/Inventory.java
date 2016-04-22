package logic.inventory;

import graphics.Camera;
import graphics.lowlevel.IndexedVertexArray;
import graphics.shaders.ShaderHandler;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.Level;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import utils.GraphicsUtils;
import utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Inventory {
    private List<InventoryItem> itemList;               // List of all items currently in the inventory
    private IndexedVertexArray backDropQuad;            // Mesh for the background of the inventory
    private static final int ITEMS_PER_ROW = 5;         // How many items will fit in one row when rendering
    private int space = ITEMS_PER_ROW * ITEMS_PER_ROW;  // Available space, different inventoryes maybe different space

    private boolean isOpen = false;                     // Whether inventory is currently opened up

    private Vector2f position;                          // Position in GUI coordinate system x:[0,1],y:[0,1], o top left
    private float baseScale = 8.0f;                     // Multiple that dictates size of inventory when rendered
    private boolean isDragging = false;                 // Whether we're currently dragging the inventory around
    private Vector2f offset;                            // Offset to drag the inventory on mouse position.

    public Inventory() {
        backDropQuad = GraphicsUtils.createInventoryQuad();
        itemList = new ArrayList<>();
        this.position = new Vector2f(0.5f, 0.0f);
    }

    // Called every tick like everything else
    public void update(Level level) {
        // Check if we are opening/closing the inventory, or if we're dragging the inventory with the mouse
        checkInput(level);
    }

    private void checkInput(Level level) {
        if (KeyInput.isKeyClicked(GLFW_KEY_G)) {
            isOpen = !isOpen;
        }

        Vector2f mousePos = new Vector2f((float)MousePosInput.getX(), (float)MousePosInput.getY());
        // Check if we right clicked and are trying to throw something out
        if (MouseButtonInput.isMouseButtonClicked(GLFW_MOUSE_BUTTON_2)) {
            if (isOpen && MathUtils.screenPointWithinInventory(mousePos, position, baseScale)) {
                // Get what item we clicked on
                Vector2f mFix = MathUtils.screenSpaceToGUI(mousePos).sub(position);
                double itemWidth = (double)baseScale/(double)(Camera.getWinSizeX()*(double)ITEMS_PER_ROW);
                double itemHeight = (double)baseScale/(double)(Camera.getWinSizeY()*(double)ITEMS_PER_ROW);
                int indX = (int)Math.floor((double)mFix.x/itemWidth);
                int indY = (int)Math.floor((double)mFix.y/itemHeight);

                int listIndex = indX + ITEMS_PER_ROW*indY;
                if (itemList.size() >= listIndex + 1) {
                    InventoryItem item = itemList.get(listIndex);
                    item.setPosition(new Vector2f(item.getPosition().x, item.getPosition().y));
                    level.addDroppedItem(item);
                    itemList.remove(item);
                }
            }
        }

        // Check the dragging status of the whole window
        if (!isDragging) {
            if (isOpen && MouseButtonInput.isMouseLeftDown() && MathUtils.screenPointWithinInventory(mousePos, position, baseScale)) {
                offset = MathUtils.screenSpaceToGUI(mousePos).sub(position);
                isDragging = true;
            }
        }
        else if (isOpen) {
            // Check if we've let go
            if (!MouseButtonInput.isMouseLeftDown()) {
                isDragging = false;
            }
            else {
                Vector2f GUIpos = MathUtils.screenSpaceToGUI(new Vector2f((float)MousePosInput.getX(), (float)MousePosInput.getY()));
                this.position.x = GUIpos.x - offset.x;
                this.position.y = GUIpos.y - offset.y;
            }
        }
    }

    public boolean getIsDragging() {return isDragging;}

    public void add(InventoryItem item) {
        itemList.add(item);
    }

    public void remove(InventoryItem item) {
        itemList.remove(item);
    }

    public void render(Matrix4f proj) {
        if (isOpen) {
            prepareRender();

            // Get world coordinates
            Vector2f worldPos = new Vector2f(
                    Camera.getPosition().x + position.x*Camera.getWinSizeX(),
                    Camera.getPosition().y + position.y*Camera.getWinSizeY());

            ShaderHandler.inventoryShader.uploadMatrix(proj, "projMatrix");
            ShaderHandler.inventoryShader.uploadMatrix(new Matrix4f().translate(worldPos.x, worldPos.y, 0f), "modelMatrix");
            ShaderHandler.inventoryShader.uploadInt(ITEMS_PER_ROW, "itemsPerRow");
            ShaderHandler.inventoryShader.uploadFloat(baseScale, "scale");
            ShaderHandler.inventoryShader.uploadFloat(baseScale, "baseScale");
            ShaderHandler.inventoryShader.uploadInt(1, "isBackground");
            backDropQuad.draw();

            ShaderHandler.inventoryShader.uploadInt(0, "isBackground");
            for (InventoryItem item : itemList) {
                item.getTexture().bind();
                ShaderHandler.inventoryShader.uploadMatrix(new Matrix4f().translate(worldPos.x, worldPos.y, 0f), "modelMatrix");
                ShaderHandler.inventoryShader.uploadInt(itemList.indexOf(item), "itemIndex");
                ShaderHandler.inventoryShader.uploadFloat(baseScale/(float)ITEMS_PER_ROW, "scale");
                item.getMesh().draw();
            }

            finishRender();
        }
    }

    private void prepareRender() {
        ShaderHandler.inventoryShader.comeHere();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private void finishRender() {
        ShaderHandler.inventoryShader.pissOff();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void cleanUp() {
        for (InventoryItem item : itemList) {
            item.cleanUp();
        }

        itemList.clear();
    }
}
