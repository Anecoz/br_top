package logic.weapons;

import graphics.lowlevel.Texture;
import logic.DrawableEntity;
import org.joml.Vector2f;

public class Ammunition extends DrawableEntity {

    protected Vector2f velocity;
    protected int damage;

    public Ammunition(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);
    }

    public void update(){
        position.add(velocity);
    }

    int getDamage(){
        return damage;
    }

    public void destroy(){
        super.cleanUp();
    }
}
