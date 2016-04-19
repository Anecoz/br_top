package logic;

import graphics.Camera;
import graphics.animation.Animation;
import graphics.lowlevel.Texture;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.weapons.Pistol;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.FileUtils;
import utils.MathUtils;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends DrawableEntity {
    private static final float SPEED = 0.1f;
    private Vector2f forward;
    private Pistol pistol;
    private Texture pistolTexture;
    private Animation walkingAnimation;

    public Player(String texFilePath) {
        super(texFilePath, new Vector2f(10), -0.3f);
        forward = new Vector2f(0);
        walkingAnimation = new Animation(FileUtils.RES_DIR + "characters/playerSpriteSheet.png", 100, 100, 8);
        walkingAnimation.start();

        //TESTING
        pistolTexture = new Texture(FileUtils.RES_DIR + "weapons/pistol.png");
        pistol = new Pistol(pistolTexture, position, -0.2f, 0.5f, 100, 50);
    }

    @Override
    public void render(Matrix4f projection) {
        super.render(projection);
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
        }

        if(KeyInput.isKeyDown(GLFW_KEY_R)){
            pistol.reload();
        }

        this.texture = walkingAnimation.getFrame();
        updateForward(proj);
        pistol.update(new Vector2f(forward));
    }

    private void updateForward(Matrix4f proj) {
        double mouseX = MousePosInput.getX();
        double mouseY = MousePosInput.getY();
        double centerX = this.position.x + this.width/2.0f;
        double centerY = this.position.y + this.height/2.0f;
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
        pistol.rotation = rotation;
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        walkingAnimation.cleanUp();
    }

    public float getSpeed() {return SPEED;}
}
