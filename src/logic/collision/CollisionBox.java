package logic.collision;

import org.joml.Vector2f;

import java.awt.*;

// Used for collision checking, holds velocity and bounding box
public class CollisionBox {
    public float x;
    public float y;
    public float w;
    public float h;
    public Vector2f vel;

    public CollisionBox(float x, float y, float w, float h, Vector2f vel) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vel = vel;
    }
}
