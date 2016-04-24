package logic;

import graphics.animation.Animation;
import input.KeyInput;
import input.MousePosInput;
import logic.inventory.Inventory;
import logic.inventory.InventoryItem;
import logic.weapons.AssaultRifle;
import logic.collision.CollisionHandler;
import logic.weapons.Pistol;
import logic.weapons.Weapon;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import utils.MathUtils;
import utils.ResourceHandler;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends DrawableEntity {
    private static final float SPEED = 0.1f;
    private Vector2f forward;
    private Pistol pistol1, pistol2, pistol3, pistol4, pistol5;
    private AssaultRifle assaultRifle;
    private Animation walkingAnimation;
    private boolean running = false;
    private Inventory inventory;
    public String displayName = "Kappa";

    public Player() {
        super(ResourceHandler.playerTexture, new Vector2f(10), -0.3f);

        forward = new Vector2f(0);
        walkingAnimation = ResourceHandler.playerAnimation;
        pistol1 = new Pistol(position, -0.2f, 1.5f, 15, 24);
        pistol2 = new Pistol(position, -0.2f, 1.5f, 15, 24);
        pistol3 = new Pistol(position, -0.2f, 1.5f, 15, 24);
        pistol4 = new Pistol(position, -0.2f, 1.5f, 15, 24);
        pistol5 = new Pistol(position, -0.2f, 1.5f, 15, 24);
        assaultRifle = new AssaultRifle(position, -0.2f, 1.5f, 40, 800);
        walkingAnimation.start();
        inventory = new Inventory();
        inventory.add(pistol1);
        inventory.add(pistol2);
        inventory.add(pistol3);
        inventory.add(pistol4);
        inventory.add(pistol5);
        inventory.add(assaultRifle);

        this.mesh = ResourceHandler.playerQuad;
    }

    @Override
    public void render(Matrix4f projection) {
        super.render(projection);
        inventory.render(projection);
    }

    public void update(Level level, Matrix4f proj) {
        updateMovement(level);
        checkPickUp(level);
        checkWeaponSwap();
        checkRunningStatus();
        inventory.update(level);

        if (!inventory.getIsDragging() && inventory.getEquipedWeapon() != null
                && GameState.gameState != GameState.GameStates.GAME_PAUSE
                && GameState.gameState != GameState.GameStates.GAME_OPTION)
            inventory.getEquipedWeapon().checkFire();

        this.texture = walkingAnimation.getFrame();
        updateForward(proj);
        if(inventory.getEquipedWeapon() != null)
            inventory.getEquipedWeapon().update(new Vector2f(forward), level);
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

    private void checkPickUp(Level level) {
        // Check whether we're picking something up
        if (KeyInput.isKeyClicked(GLFW_KEY_F)) {
            InventoryItem item = level.getClosestItemAt(position);

            if (item != null) {
                item.setPosition(position);
                inventory.add(item);
            }
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
            //equipedWeapon = assaultRifle;
        }
        if(KeyInput.isKeyClicked(GLFW_KEY_2)) {
            //equipedWeapon = pistol1;
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
        Vector2f worldMouse = MathUtils.screenSpaceToWorld(new Vector2f((float) mouseX, (float) mouseY), GameState.WIDTH, GameState.HEIGHT, proj);
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

        if(inventory.getEquipedWeapon() != null)
            inventory.getEquipedWeapon().rotation = new Matrix4f(rotation);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        //walkingAnimation.cleanUp();
        inventory.cleanUp();
    }

    public float getSpeed() {return SPEED;}
    public Vector2f getForward() {return forward;}
}
