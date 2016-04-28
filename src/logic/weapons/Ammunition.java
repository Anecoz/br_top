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
    public boolean dead = false;
    protected CollisionBox box;

    // Some optimization vectors
    private Vector2f up = new Vector2f(0f, -1f);
    private Vector3f center = new Vector3f(0f, 0f, -0.3f);
    private Rectangle tileRect = new Rectangle(0, 0, 1, 1);

    public Ammunition(Texture sprite, Texture displaySprite, Vector2f position, float layer, int uniqueId) {
        super(sprite, displaySprite, position, layer, uniqueId);

        box = new CollisionBox(
                position.x,
                position.y,
                this.width,
                this.height,
                new Vector2f(0)
        );
    }

    public void update(Level level){
        // Do a collision test with 8 adjacent tiles
        box.x = position.x;
        box.y = position.y;
        box.vel.x = this.velocity.x;
        box.vel.y = this.velocity.y;

        int tileX = (int)this.position.x;
        int tileY = (int)this.position.y;
        boolean didCollide = false;
        outerloop:
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                // Is there even a collision here?
                if (level.getCollAt(tileX + x, tileY + y)) {
                    tileRect.x = tileX + x;
                    tileRect.y = tileY + y;
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

        center.x = (float)centerX;
        center.y = (float)centerY;
        rotation.identity()
                .translate(center)
                .rotate(velocity.angle(up), 0.0f, 0.0f, -1.0f)
                .translate(center.negate());
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    public CollisionBox getCollisionBox() {
        return box;
    }

    public int getUniqueId() {
        return this.uniqueId;
    }
}
