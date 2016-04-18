package logic;

import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import graphics.shaders.ShaderHandler;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.FileUtils;
import utils.GraphicsUtils;

// Any entity that can be drawn to screen
public class DrawableEntity {

    protected Texture texture;
    protected Vector2f position;
    protected IndexedVertexArray mesh;
    protected Matrix4f rotation;

    public Vector2f getPosition() {return position;}

    public DrawableEntity(String texFilePath, Vector2f initPos) {
        this.texture = new Texture(FileUtils.RES_DIR + texFilePath);
        this.position = initPos;
        mesh = GraphicsUtils.createModelQuad();
        this.rotation = new Matrix4f();
    }

    public DrawableEntity(Texture texture, Vector2f initPos) {
        this.texture = texture;
        this.position = initPos;
        mesh = GraphicsUtils.createModelQuad();
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
