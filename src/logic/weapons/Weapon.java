package logic.weapons;

import graphics.lowlevel.IndexedVertexArray;
import graphics.shaders.Shader;
import graphics.lowlevel.Texture;
import graphics.shaders.ShaderHandler;
import logic.DrawableEntity;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.GraphicsUtils;

public class Weapon extends DrawableEntity {

    protected float reloadTime;
    protected int magazineSize;     // Max magazine size
    protected int magazine;         // Ammunition loaded in weapon
    protected int ammo;             // Current reserve ammunition
    protected int roundsPerMinute;

    public Weapon(Texture sprite, Vector2f position, float layer) {
        super(sprite, position, layer);
    }

    public void update(){

    }

    public void fire(){
        //TODO:Add rounds per minute (rpm) limit.
        if(magazine > 0) {
            magazine--;
            System.out.println(magazine + "/" + ammo);
        }
    }

    public void reload(){
        // TODO:Add reload time.
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
}
