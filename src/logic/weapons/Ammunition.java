package logic.weapons;

import graphics.lowlevel.Texture;
import logic.DrawableEntity;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ammunition extends DrawableEntity {

    protected Vector2f velocity;
    protected int damage;

    public Ammunition(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);
    }

    public void update(){
        position.add(velocity);

        // Update rotation matrix
        double centerX = this.position.x + this.width/2.0f;
        double centerY = this.position.y + this.height/2.0f;

        Vector3f center = new Vector3f((float) centerX, (float) centerY, -0.3f);
        Vector2f up = new Vector2f(0.0f, -1.0f);
        rotation = new Matrix4f()
                .translate(center)
                .rotate(velocity.angle(up), 0.0f, 0.0f, -1.0f)
                .translate(center.negate());
    }

    int getDamage(){
        return damage;
    }

    public void destroy(){
        super.cleanUp();
    }
}
