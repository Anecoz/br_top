package logic.weapons;

import org.joml.Vector2f;
import utils.ResourceHandler;

public class Pistol extends Weapon {

    public Pistol(Vector2f position, float layer, float reloadTime, int magazineSize, int roundsPerMinute) {
        super(ResourceHandler.pistolTexture, ResourceHandler.pistolTexture, position, layer);

        this.reloadTime = reloadTime;
        this.magazineSize = magazineSize;
        this.magazine = magazineSize;
        this.ammo = magazineSize;
        this.roundsPerMinute = roundsPerMinute;
        this.position = position;
        this.isAutomatic = false;
        this.isReloading = false;
        this.isFiring = false;
        this.spawnBullet = false;
        this.displayName = "Peestol";
    }
}
