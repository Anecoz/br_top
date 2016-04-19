package logic;

import graphics.animation.Animation;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.weapons.AssaultRifle;
import logic.weapons.Pistol;
import logic.weapons.Weapon;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.MathUtils;
import utils.ResourceHandler;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends DrawableEntity {
    private static final float SPEED = 0.1f;
    private Vector2f forward;
    private Pistol pistol;
    private AssaultRifle assaultRifle;
    private Animation walkingAnimation;
    private boolean running = false;

    private List<Weapon> weaponList = new ArrayList<>();
    private Weapon equipedWeapon;

    public Player() {
        super(ResourceHandler.playerTexture, new Vector2f(10), -0.3f);

        forward = new Vector2f(0);
        walkingAnimation = ResourceHandler.playerAnimation;
        pistol = new Pistol(position, -0.2f, 1.5f, 15, 24);
        assaultRifle = new AssaultRifle(position, -0.2f, 3.5f, 40, 600);
        weaponList.add(pistol);
        weaponList.add(assaultRifle);
        equipedWeapon = pistol;
        walkingAnimation.start();
    }

    @Override
    public void render(Matrix4f projection) {
        super.render(projection);
        equipedWeapon.render(projection);
    }

    public void update(Level level, Matrix4f proj) {
        updateMovement(level);
        checkWeaponSwap();
        checkRunningStatus();
        checkFire();

        this.texture = walkingAnimation.getFrame();
        updateForward(proj);
        equipedWeapon.update(new Vector2f(forward));
    }

    private void updateMovement(Level level) {
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
    }

    private void checkRunningStatus() {
        if (anyRunningKeysDown())
            if (!running) {
                running = true;
                walkingAnimation.start();
            }

        if (noRunningKeysDown()) {
            running = false;
            walkingAnimation.stop();
        }
    }

    private void checkWeaponSwap(){
        if(KeyInput.isKeyClicked(GLFW_KEY_1)) {
            equipedWeapon = assaultRifle;
        }
        if(KeyInput.isKeyClicked(GLFW_KEY_2)) {
            equipedWeapon = pistol;
        }
    }

    private void checkFire() {
        if(equipedWeapon.isAutomatic()){
            if (MouseButtonInput.isMouseLeftDown()) {
                equipedWeapon.fire();
            }
        } else {

            if (MouseButtonInput.isMouseButtonClicked(GLFW_MOUSE_BUTTON_1)) {
                equipedWeapon.fire();
            }
        }

        if(KeyInput.isKeyClicked(GLFW_KEY_R)) {
            equipedWeapon.reload();
        }
    }

    private boolean anyRunningKeysDown() {
        return (KeyInput.isKeyDown(GLFW_KEY_A) || KeyInput.isKeyDown(GLFW_KEY_S) ||
                KeyInput.isKeyDown(GLFW_KEY_D) || KeyInput.isKeyDown(GLFW_KEY_W));
    }

    private boolean noRunningKeysDown() {
        return (!KeyInput.isKeyDown(GLFW_KEY_A) && !KeyInput.isKeyDown(GLFW_KEY_S) &&
                !KeyInput.isKeyDown(GLFW_KEY_D) && !KeyInput.isKeyDown(GLFW_KEY_W));
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
        equipedWeapon.rotation = rotation;
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        walkingAnimation.cleanUp();
        for(Weapon wep : weaponList){
            wep.cleanUp();
        }
    }

    public float getSpeed() {return SPEED;}
}
