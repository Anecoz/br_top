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
import networking.client.ClientSender;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import utils.MathUtils;
import utils.ResourceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends DrawableEntity {
    private static final float SPEED = 0.06f;
    private Vector2f forward;
    private Animation walkingAnimation;
    private boolean running = false;
    private Inventory inventory;
    public String displayName = "Kappa";

    private static ConcurrentHashMap<Integer, Vector2i> pickupQueue;

    // Some optimization vectors
    private Vector2f tmp = new Vector2f(0);
    private Vector2f mouse = new Vector2f(0);
    private Vector3f center = new Vector3f(0f, 0f, -0.3f);
    private Vector2f up = new Vector2f(0f, -1.0f);

    public Player() {
        super(ResourceHandler.playerTexture, new Vector2f(10), -0.3f);

        forward = new Vector2f(0);
        walkingAnimation = ResourceHandler.playerAnimation;
        walkingAnimation.start();
        inventory = new Inventory();

        inventory.add(new AssaultRifle(position, -0.2f, 1.5f, AssaultRifle.MAG_SIZE, 40, 800, 10));

        this.mesh = ResourceHandler.playerQuad;
        pickupQueue = new ConcurrentHashMap<>();
    }

    @Override
    public void render(Matrix4f projection) {
        super.render(projection);
        inventory.render(projection);
    }

    public static void addPickupRequest(Vector2i position, int uniqueId) {
        pickupQueue.put(uniqueId, position);
    }

    public void update(Level level, Matrix4f proj) {
        updateMovement(level);
        checkPickUp(level);
        checkWeaponSwap();
        checkRunningStatus();
        checkPickupQueue(level);
        inventory.update(level);

        if (!inventory.getIsDragging() && inventory.getEquipedWeapon() != null
                && GameState.gameState != GameState.GameStates.GAME_PAUSE
                && GameState.gameState != GameState.GameStates.GAME_OPTION)
            inventory.getEquipedWeapon().checkFire();

        this.texture = walkingAnimation.getFrame();
        updateForward(proj);
        if(inventory.getEquipedWeapon() != null)
            inventory.getEquipedWeapon().update(forward);
    }

    private synchronized void checkPickupQueue(Level level) {
        if (pickupQueue.size() > 0) {
            for (ConcurrentHashMap.Entry<Integer, Vector2i> entry : pickupQueue.entrySet()) {
                InventoryItem item = level.getDroppedItemById(entry.getValue(), entry.getKey());
                if (item != null) {
                    item.setPosition(position);
                    inventory.add(item);
                }
            }
        }
        pickupQueue.clear();
    }

    private void updateMovement(Level level) {
        tmp.x = position.x;
        tmp.y = position.y;
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
            // -----WARNING----- THIS DOES __NOT__ REMOVE THE ITEM FROM THE LEVEL, YOU ARE HOLDING A REF HERE
            InventoryItem item = level.getClosestItemAt(position);
            if (item != null) {
                ClientSender.sendPickupRequest(item);
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
        mouse.x = (float)mouseX;
        mouse.y = (float)mouseY;
        MathUtils.screenSpaceToWorld(mouse, GameState.WIDTH, GameState.HEIGHT, proj);
        // Get vector
        this.forward.x = mouse.x - (float) centerX;
        this.forward.y = mouse.y - (float) centerY;
        this.forward.normalize();
        // Update rotation matrix
        center.x = (float)centerX;
        center.y = (float)centerY;
        rotation.identity()
                .translate(center)
                .rotate(forward.angle(up), 0.0f, 0.0f, -1.0f)
                .translate(center.negate());

        if(inventory.getEquipedWeapon() != null)
            inventory.getEquipedWeapon().rotation = rotation;
    }

    public void cleanUp() {
        inventory.cleanUp();
    }

    public float getSpeed() {return SPEED;}
    public Vector2f getForward() {return forward;}
}
