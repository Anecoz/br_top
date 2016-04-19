package logic.weapons;

import graphics.lowlevel.Texture;
import logic.DrawableEntity;
import org.joml.Vector2f;

public class Weapon extends DrawableEntity {

    protected float reloadTime;
    protected int magazineSize;     // Max magazine size
    protected int magazine;         // Ammunition loaded in weapon
    protected int ammo;             // Current reserve ammunition
    protected int roundsPerMinute;
    protected boolean isReloading;
    protected boolean automatic;
    protected Vector2f forward;

    public Weapon(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);
    }

    public void update(Vector2f forward){
        this.forward = forward;
    }

    public void fire(){
        //TODO:Add rounds per minute (rpm) limit in the individual weapon.
        if(magazine > 0) {
            magazine--;
            System.out.println(magazine + "/" + ammo);
        }
    }

    public void reload(){
        // Reload time is in the individual weapon,
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
        return automatic;
    }
}
