package logic.weapons;

import graphics.shaders.Shader;
import graphics.lowlevel.Texture;
import org.joml.Vector2f;

public class Bullet extends Ammunition {

    public Bullet(Texture sprite, Vector2f position, Vector2f velocity, int damage){
        super(sprite, position);

        this.damage = damage;
        this.velocity = velocity;
    }
}
