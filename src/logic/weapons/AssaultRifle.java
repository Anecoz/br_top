package logic.weapons;

import org.joml.Vector2f;
import utils.ResourceHandler;

public class AssaultRifle  extends Weapon {

    public AssaultRifle(Vector2f position, float layer, float reloadTime, int magazineSize, int roundsPerMinute) {
        super(ResourceHandler.pistolTexture, position, layer);

        this.reloadTime = reloadTime;
        this.magazineSize = magazineSize;
        this.magazine = magazineSize;
        this.ammo = magazineSize;
        this.roundsPerMinute = roundsPerMinute;
        this.position = position;
        this.isAutomatic = true;
        this.isReloading = false;
        this.isFiring = false;
        this.spawnBullet = false;
    }
}
