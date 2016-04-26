package logic.weapons;

import org.joml.Vector2f;
import utils.ResourceHandler;

public class AssaultRifle  extends Weapon {

    public static final int MAG_SIZE = 40;

    public AssaultRifle(Vector2f position, float layer, float reloadTime, int currentMagazine, int ammo, int roundsPerMinute, int uniqueId) {
        super(ResourceHandler.assaultRifleTexture, ResourceHandler.assaultRifleTextureDisplay, position, layer, uniqueId);

        this.reloadTime = reloadTime;
        this.magazineSize = MAG_SIZE;
        this.magazine = currentMagazine;
        this.ammo = ammo;
        this.roundsPerMinute = roundsPerMinute;
        this.position = position;
        this.isAutomatic = true;
        this.isReloading = false;
        this.isFiring = false;
        this.spawnBullet = false;
        this.displayName = "Ratata";

        this.mesh = ResourceHandler.assaultRifleQuad;
    }
}
