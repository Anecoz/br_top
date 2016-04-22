package logic.inventory;

import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import logic.DrawableEntity;
import org.joml.Vector2f;

public abstract class InventoryItem extends DrawableEntity {
    protected String displayName;

    public InventoryItem(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);
    }

    public Texture getTexture() {
        return this.texture;
    }

    public IndexedVertexArray getMesh() {
        return this.mesh;
    }
}
