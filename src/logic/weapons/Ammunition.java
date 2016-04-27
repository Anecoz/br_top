package logic.weapons;

import graphics.lowlevel.Texture;
import logic.Level;
import logic.collision.CollisionBox;
import logic.collision.CollisionHandler;
import logic.inventory.InventoryItem;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;

public abstract class Ammunition extends InventoryItem {

    protected Vector2f velocity;
    protected int damage;
    protected boolean dead = false;

    public Ammunition(Texture sprite, Texture displaySprite, Vector2f position, float layer, int uniqueId) {
        super(sprite, displaySprite, position, layer, uniqueId);
    }

    public void update(Level level){
        // Do a collision test with 8 adjacent tiles
        CollisionBox box = new CollisionBox(
                position.x,
                position.y,
                this.width,
                this.height,
                new Vector2f(this.velocity.x, this.velocity.y));

        int tileX = (int)this.position.x;
        int tileY = (int)this.position.y;
        boolean didCollide = false;
        outerloop:
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                // Is there even a collision here?
                if (level.getCollAt(tileX + x, tileY + y)) {
                    Rectangle tileRect = new Rectangle(tileX + x, tileY + y, 1, 1);
                    float collTime = CollisionHandler.sweptAABBCollision(box, tileRect);
                    // if collision
                    if (collTime > 0.0f && collTime < 1.0f) {
                        position.add(velocity.mul(collTime));
                        didCollide = true;
                        this.dead = true;
                        break outerloop;
                    }
                }
            }

        if (!didCollide) {
            position.add(velocity);
        }
        updateRotation();
    }

    private void updateRotation() {
        double centerX = this.position.x + this.width/2.0f;
        double centerY = this.position.y + this.height/2.0f;

        Vector3f center = new Vector3f((float) centerX, (float) centerY, -0.3f);
        Vector2f up = new Vector2f(0.0f, -1.0f);
        rotation = new Matrix4f()
                .translate(center)
                .rotate(velocity.angle(up), 0.0f, 0.0f, -1.0f)
                .translate(center.negate());
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    public int getUniqueId() {
        return this.uniqueId;
    }

    public int getDamage(){
        return this.damage;
    }
}
