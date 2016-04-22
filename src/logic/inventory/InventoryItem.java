package logic.inventory;

import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import logic.DrawableEntity;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public abstract class InventoryItem extends DrawableEntity {
    protected String displayName;

    public InventoryItem(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);
    }

    public void setPosition(Vector2f pos) {this.position = pos;}

    public Texture getTexture() {
        return this.texture;
    }

    public IndexedVertexArray getMesh() {
        return this.mesh;
    }

    public void setRotation(Matrix4f rotation){
        this.rotation = rotation;
    }
}
