package logic.weapons;

import graphics.IndexedVertexArray;
import graphics.Shader;
import graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.GraphicsUtils;

public class Ammunition {

    protected Vector2f velocity;
    protected int damage;
    protected Texture sprite;
    protected Vector2f position;
    private IndexedVertexArray mesh;
    private Shader shader;
    private Matrix4f rotation;

    public Ammunition(Shader shader, Texture sprite, Vector2f position){
        this.sprite = sprite;
        this.position = position;
        this.shader = shader;
        rotation = new Matrix4f();
        mesh = GraphicsUtils.createModelQuad();
    }

    public void update(){
        position.add(velocity);
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

    int getDamage(){
        return damage;
    }

    public void Destroy(){
        sprite.cleanUp();
    }
}
