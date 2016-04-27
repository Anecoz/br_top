package logic.weapons;

import gui.fontMeshCreator.GUIText;
import graphics.lowlevel.Texture;
import input.KeyInput;
import input.MouseButtonInput;
import logic.Level;
import logic.inventory.InventoryItem;
import networking.client.ClientSender;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.ResourceHandler;
import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public abstract class Weapon extends InventoryItem {

    protected float reloadTime;
    protected int magazineSize;     // Max magazine size
    protected int magazine;         // Ammunition loaded in weapon
    protected int ammo;             // Current reserve ammunition
    protected int roundsPerMinute;
    protected boolean isReloading;
    protected boolean isFiring;
    protected boolean isAutomatic;
    protected Vector2f forward;
    protected Timer reloadTimer;
    protected Timer shootTimer;
    protected boolean spawnBullet;
    protected static GUIText text;

    // Some optimizing things
    private Vector2f ammoPos = new Vector2f(0.05f, 0.9f);

    public Weapon(Texture sprite, Texture displaySprite, Vector2f position, float layer, int uniqueId) {
        super(sprite, displaySprite, position, layer, uniqueId);

        reloadTimer = new Timer();
        shootTimer = new Timer();
        forward = new Vector2f(0);
    }

    public void update(Vector2f forward){
        this.forward.x = forward.x;
        this.forward.y = forward.y;

        if(spawnBullet) {
            Bullet bullet = spawnBullet();
            Level.ammunitionList.add(bullet);
            ClientSender.spawnProjectile(bullet);
            spawnBullet = false;
        }

        if(!isFiring)
            shootTimer.cancel();

        if (text != null)
            text.remove();
        text = new GUIText(Integer.toString(magazine) + "/ " + Integer.toString(ammo),
                2,
                ResourceHandler.font,
                ammoPos,
                0.3f,
                false);
        text.setColour(1, 1, 1);
    }

    @Override
    public void render(Matrix4f projection){
        super.render(projection);
    }

    public void checkFire() {
        if(isAutomatic){
            if (MouseButtonInput.isMouseLeftDown()) {
                fire();
            } else {
                isFiring = false;
            }
        } else {
            if (MouseButtonInput.isMouseButtonClicked(GLFW_MOUSE_BUTTON_1)) {
                fire();
            } else {
                isFiring = false;
            }
        }

        if(KeyInput.isKeyClicked(GLFW_KEY_R)) {
            reload();
        }
    }

    public void fire(){
        if (magazine < 1) {
            // TODO: Notify the player of empty magazine
        } else {
            if(!isReloading && !isFiring) {
                isFiring = true;
                shootTimer = new Timer();
                shootTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if(magazine > 0) {
                            spawnBullet = true;
                            magazine--;
                        }
                    }
                },0 , (long)(1000/((float)(roundsPerMinute/60))));
            }
        }
    }

    public void reload(){
        if(ammo > 0 && magazine < magazineSize) {
            isReloading = true;
            // TODO: Play animation with reloadTime duration.
            reloadTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isReloading = false;
                    if(magazine != magazineSize) {
                        if(ammo > 0) {
                            ammo -= (magazineSize - magazine);
                            magazine += (magazineSize - magazine);
                            if(ammo <= 0) {
                                magazine += ammo;
                                ammo = 0;
                            }
                            if(magazine >= magazineSize)
                                magazine = magazineSize;
                        }
                    }
                    System.out.println("Reloaded!");
                }
            }, (long) (reloadTime * 1000));
        }

    }

    private Bullet spawnBullet(){
        Vector2f bulletPos = new Vector2f(
                this.position.x + this.width / 2.0f
                        - ResourceHandler.bulletTexture.getWidthAfterScale() / 2.0f,
                this.position.y + this.height / 2.0f
                        - ResourceHandler.bulletTexture.getHeightAfterScale() / 2.0f);
        Vector2f bulletVel = new Vector2f(this.forward.x, this.forward.y);
        return new Bullet(bulletPos, bulletVel.mul(0.05f), 0);
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

    public float getReloadTime() {
        return reloadTime;
    }

    public boolean isAutomatic(){
        return isAutomatic;
    }

    public void setFiringBool(boolean value) {
        isFiring = value;
    }

    public void cleanUp() {
        reloadTimer.cancel();
        shootTimer.cancel();
        text.remove();
    }
}
