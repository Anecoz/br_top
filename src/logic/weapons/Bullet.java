package logic.weapons;

import org.joml.Vector2f;
import utils.ResourceHandler;

public class Bullet extends Ammunition {
    public Bullet(Vector2f position, Vector2f velocity, int damage, int uniqueId){
        super(ResourceHandler.bulletTexture, ResourceHandler.bulletTexture, position, damage,-0.8f, uniqueId);
        this.velocity = velocity;
        this.mesh = ResourceHandler.bulletQuad;
    }
}
