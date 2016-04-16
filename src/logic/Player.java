package logic;


import graphics.Camera;
import graphics.IndexedVertexArray;
import graphics.Shader;
import graphics.Texture;
import input.Input;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.FileUtils;
import utils.GraphicsUtils;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private static final float SPEED = 0.1f;
    private Vector2f position;
    private Texture texture;
    private IndexedVertexArray mesh;
    private Shader shader;
    private int width;
    private int height;
    private float size;

    public Player(String texFilePath) {
        position = new Vector2f(0);
        texture = new Texture(FileUtils.RES_DIR + texFilePath);
        mesh = GraphicsUtils.createModelQuad();
        shader = new Shader("player.vert", "player.frag");
        shader.uploadTexture(0, "tex");
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.size = 0.5f;
    }

    public float getSize() {return size;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void render(Matrix4f projection) {
        shader.comeHere();
        texture.bind();

        shader.uploadMatrix(projection, "projMatrix");
        shader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        mesh.draw();

        texture.bind();
        shader.pissOff();
    }

    public void update(Camera cam, Level level) {
        Vector2f tmp = new Vector2f(position.x, position.y);

        if (Input.isKeyDown(GLFW_KEY_W))
            position.y -= SPEED;
        if (Input.isKeyDown(GLFW_KEY_S))
            position.y += SPEED;
        if (Input.isKeyDown(GLFW_KEY_A))
            position.x -= SPEED;
        if (Input.isKeyDown(GLFW_KEY_D))
            position.x += SPEED;

        if (CollisionHandler.checkPlayerCollision(this, level)) {
            position = tmp;
        }

    }

    public Vector2f getPosition() {return position;}

}
