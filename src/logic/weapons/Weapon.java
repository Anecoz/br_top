package logic.weapons;

import fontMeshCreator.GUIText;
import graphics.lowlevel.Texture;
import input.KeyInput;
import input.MouseButtonInput;
import logic.DrawableEntity;
import logic.Level;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.ResourceHandler;

import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class Weapon extends DrawableEntity {

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
    protected List<Ammunition> ammunitionList = new ArrayList<>();
    protected Vector2f position;
    protected boolean spawnBullet;
    protected static GUIText text;

    public Weapon(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);

        reloadTimer = new Timer();
        shootTimer = new Timer();
    }

    public void update(Vector2f forward, Level level){
        this.forward = forward;
        // Using iterator so that we can remove bullets
        Iterator<Ammunition> i = ammunitionList.iterator();
        while (i.hasNext()){
            Ammunition bullet = i.next();
            if (bullet.dead) {
                i.remove();
            }
            else {
                bullet.update(level);
            }
        }

        if(spawnBullet) {
            ammunitionList.add(spawnBullet());
            spawnBullet = false;
        }

        if(!isFiring)
            shootTimer.cancel();

        if (text != null)
            text.remove();
        text = new GUIText(Integer.toString(magazine) + "/ " + Integer.toString(ammo),
                2,
                ResourceHandler.font,
                new Vector2f(0.05f, 0.9f),
                0.3f,
                false);
        text.setColour(1, 1, 1);

    }

    @Override
    public void render(Matrix4f projection){
        super.render(projection);
        for(Ammunition bullet: ammunitionList){
            bullet.render(projection);
        }
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
                            System.out.println(magazine + "/" + ammo);
                        }
                    }
                },0 , (long)(1000/((float)(roundsPerMinute/60))));
            }
        }
    }

    public void reload(){
        if(ammo > 0) {
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
                                System.out.println(magazine + "/" + ammo);
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
                position.x + this.width / 2.0f
                        - ResourceHandler.bulletTexture.getWidthAfterScale() / 2.0f,
                position.y + this.height / 2.0f
                        - ResourceHandler.bulletTexture.getHeightAfterScale() / 2.0f);
        Vector2f bulletVel = new Vector2f(this.forward.x, this.forward.y);
        return new Bullet(bulletPos, bulletVel.mul(0.6f), -0.8f, 10);
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
    }
}
