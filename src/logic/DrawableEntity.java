package logic;

import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import graphics.shaders.ShaderHandler;
import logic.weapons.Bullet;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.FileUtils;
import utils.GraphicsUtils;

// Any entity that can be drawn to screen
public abstract class DrawableEntity {

    protected Texture texture;
    protected Vector2f position;
    protected IndexedVertexArray mesh;
    protected Matrix4f rotation;
    protected float width;
    protected float height;
    protected float scale;

    public float getWidth() {return width;}
    public float getHeight() {return height;}
    public Vector2f getPosition() {return position;}
    public Matrix4f getRotation() {return rotation;}

    /*public DrawableEntity(String texFilePath, Vector2f initPos, float layer) {
        this.texture = new Texture(FileUtils.RES_DIR + texFilePath);
        init(layer, initPos);
    }*/

    public DrawableEntity(Texture texture, Vector2f initPos, float layer) {
        this.texture = texture;
        init(layer, initPos);
    }

    private void init(float layer, Vector2f initPos) {
        this.width = this.texture.getWidthAfterScale();
        this.height = this.texture.getHeightAfterScale();
        this.scale = this.texture.getScale();

        this.position = initPos;
        mesh = GraphicsUtils.createModelQuad(width, height, layer);
        this.rotation = new Matrix4f();
    }

    public void render(Matrix4f projection) {
        ShaderHandler.standardShader.comeHere();
        texture.bind();

        ShaderHandler.standardShader.uploadMatrix(projection, "projMatrix");
        ShaderHandler.standardShader.uploadMatrix(rotation, "rotationMatrix");
        ShaderHandler.standardShader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        mesh.draw();

        texture.unbind();
        ShaderHandler.standardShader.pissOff();
    }

    public void cleanUp() {
        mesh.cleanUp();
        texture.cleanUp();
    }
}
