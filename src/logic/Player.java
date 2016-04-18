package logic;

import graphics.Camera;
import graphics.IndexedVertexArray;
import graphics.Shader;
import graphics.Texture;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.weapons.Pistol;
import logic.weapons.Weapon;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.FileUtils;
import utils.GraphicsUtils;
import utils.MathUtils;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private static final float SPEED = 0.1f;
    private Vector2f position;
    private Vector2f forward;
    private Texture texture;
    private IndexedVertexArray mesh;
    private Shader shader;
    private int width;
    private int height;
    private float size;
    private Matrix4f rotation;

    private Pistol pistol;
    private Texture pistolTexture;

    public Player(String texFilePath) {
        position = new Vector2f(10);
        forward = new Vector2f(0);
        rotation = new Matrix4f();
        texture = new Texture(FileUtils.RES_DIR + texFilePath);
        mesh = GraphicsUtils.createModelQuad();
        shader = new Shader("player.vert", "player.frag");
        shader.comeHere();
        shader.uploadTexture(0, "tex");
        shader.pissOff();
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.size = 0.5f;

        //TESTING
        pistolTexture = new Texture(FileUtils.RES_DIR + "weapons/pistol.png");
        pistol = new Pistol(shader, pistolTexture, position, 0.5f, 15, 50);
    }

    public float getSize() {return size;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void render(Matrix4f projection) {
        shader.comeHere();
        texture.bind();

        shader.uploadMatrix(projection, "projMatrix");
        shader.uploadMatrix(rotation, "rotationMatrix");
        shader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        mesh.draw();

        texture.unbind();
        shader.pissOff();

        pistol.render(projection);
    }

    public void update(Camera cam, Level level, Matrix4f proj) {
        Vector2f tmp = new Vector2f(position.x, position.y);
        if (KeyInput.isKeyDown(GLFW_KEY_W)) {
            position.y -= SPEED;
            if (CollisionHandler.checkPlayerCollision(this, level))
                position.y = tmp.y;
        }
        if (KeyInput.isKeyDown(GLFW_KEY_S)) {
            position.y += SPEED;
            if (CollisionHandler.checkPlayerCollision(this, level))
                position.y = tmp.y;
        }
        if (KeyInput.isKeyDown(GLFW_KEY_A)) {
            position.x -= SPEED;
            if (CollisionHandler.checkPlayerCollision(this, level))
                position.x = tmp.x;
        }
        if (KeyInput.isKeyDown(GLFW_KEY_D)) {
            position.x += SPEED;
            if (CollisionHandler.checkPlayerCollision(this, level))
                position.x = tmp.x;
        }

        if(MouseButtonInput.isLeftDown()){
            pistol.fire();
            System.out.println(pistol.getMagazine() + "/" + pistol.getAmmo());
        }

        if(KeyInput.isKeyDown(GLFW_KEY_R)){
            pistol.reload();
            System.out.println(pistol.getMagazine() + "/" + pistol.getAmmo());
        }

        updateFoward(proj);
    }

    private void updateFoward(Matrix4f proj) {
        double mouseX = MousePosInput.getX();
        double mouseY = MousePosInput.getY();
        // Size is already "halved" because of a smaller bounding box for collision detection
        double centerX = this.position.x + this.size;
        double centerY = this.position.y + this.size;
        Vector2f worldMouse = MathUtils.screenSpaceToWorld(new Vector2f((float) mouseX, (float) mouseY), 1280, 720, proj);
        // Get vector
        this.forward.x = worldMouse.x - (float) centerX;
        this.forward.y = worldMouse.y - (float) centerY;
        this.forward.normalize();
        // Update rotation matrix
        Vector3f center = new Vector3f((float) centerX, (float) centerY, -0.3f);
        Vector2f up = new Vector2f(0.0f, -1.0f);
        rotation = new Matrix4f()
                .translate(center)
                .rotate(forward.angle(up), 0.0f, 0.0f, -1.0f)
                .translate(center.negate());
    }

    public Vector2f getPosition() {return position;}
    public float getSpeed() {return SPEED;}
}
