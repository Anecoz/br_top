package logic.weapons;

import graphics.Shader;
import graphics.Texture;
import org.joml.Vector2f;

public class Bullet extends Ammunition {

    public Bullet(Shader shader, Texture sprite, Vector2f position, Vector2f velocity, int damage){
        super(shader, sprite, position);

        this.damage = damage;
        this.velocity = velocity;
    }
}
