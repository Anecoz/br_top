package logic.inventory;

import graphics.lowlevel.IndexedVertexArray;
import graphics.shaders.ShaderHandler;
import logic.GameState;
import org.lwjgl.opengl.GL11;
import utils.GraphicsUtils;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<InventoryItem> itemList;
    private IndexedVertexArray backDropQuad;
    private int itemsPerRow = 5;

    public Inventory() {
        backDropQuad = GraphicsUtils.createInventoryQuad();
        itemList = new ArrayList<>();
    }

    public void add(InventoryItem item) {
        itemList.add(item);
    }

    public void remove(InventoryItem item) {
        itemList.remove(item);
    }

    public void render() {
        prepareRender();

        ShaderHandler.inventoryShader.uploadFloat((float) GameState.HEIGHT/(float) GameState.WIDTH, "aspectRatio");
        ShaderHandler.inventoryShader.uploadInt(1, "isBackground");
        backDropQuad.draw();

        ShaderHandler.inventoryShader.uploadInt(0, "isBackground");
        for (InventoryItem item : itemList) {
            item.getTexture().bind();
            ShaderHandler.inventoryShader.uploadFloat(0.2f, "scale");
            item.getMesh().draw();
        }
        
        finishRender();
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
}
