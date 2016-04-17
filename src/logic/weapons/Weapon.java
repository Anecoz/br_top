package logic.weapons;

import graphics.IndexedVertexArray;
import graphics.Shader;
import graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.GraphicsUtils;

public class Weapon {

    protected Texture sprite;
    protected Vector2f position;
    protected float reloadTime;
    protected int magazineSize;     // Max magazine size
    protected int magazine;         // Ammunition loaded in weapon
    protected int ammo;             // Current reserve ammunition
    protected int roundsPerMinute;
    private IndexedVertexArray mesh;
    private Shader shader;
    private Matrix4f rotation;

    public Weapon(Shader shader, Texture sprite, Vector2f position){
        this.shader = shader;
        this.sprite = sprite;
        this.position = position;
        rotation = new Matrix4f();
        mesh = GraphicsUtils.createModelQuad();
    }

    public void update(){

    }

    public void render(Matrix4f projection){
        shader.comeHere();
        sprite.bind();

        shader.uploadMatrix(projection, "projMatrix");
        shader.uploadMatrix(rotation, "rotationMatrix");
        shader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        mesh.draw();

        sprite.unbind();
        shader.pissOff();
    }

    public void fire(){
        //TODO:Add rounds per minute (rpm) limit.
        if(magazine > 0)
            magazine--;
    }

    public void reload(){
        // TODO:Add reload time.
        if(magazine != magazineSize) {
            if(ammo > 0) {
                ammo -= (magazineSize - magazine);
                magazine = magazineSize;
            }
        }
    }

    public void addAmmo(int value){
        ammo += value;
    }

    public int getMagazine(){
        return magazine;
    }

    public int getMagazineSize(){
        return magazineSize;
    }

    public int getAmmo(){
        return ammo;
    }

    public void cleanUp(){
        sprite.cleanUp();
    }
}
