package logic.weapons;

import org.joml.Vector2f;
import utils.ResourceHandler;

public class Bullet extends Ammunition {

    public Bullet(Vector2f position, Vector2f velocity, float layer, int damage){
        super(ResourceHandler.bulletTexture, ResourceHandler.bulletTexture, position, layer);

        this.damage = damage;
        this.velocity = velocity;
    }
}
