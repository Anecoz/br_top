package logic.inventory;

import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import logic.DrawableEntity;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.ResourceHandler;

public abstract class InventoryItem extends DrawableEntity {
    protected String displayName;
    protected Texture displayTexture;
    protected int uniqueId;

    public InventoryItem(Texture sprite, Texture displayTexture, Vector2f position, float layer, int uniqueId) {
        super(sprite, position, layer);
        this.displayTexture = displayTexture;
        this.uniqueId = uniqueId;
    }

    public int getUniqueId() {
        return this.uniqueId;
    }

    public void setPosition(Vector2f pos) {
        this.position = pos;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Texture getDisplayTexture() {
        return this.displayTexture;
    }

    public IndexedVertexArray getMesh() {
        return this.mesh;
    }

    public void setRotation(Matrix4f rotation){
        this.rotation = rotation;
    }

    public void cleanUp(){

    }
}
