package logic.weapons;

import org.joml.Vector2f;
import utils.ResourceHandler;

public class Pistol extends Weapon {

    public static int magazineSize = 15;

    public Pistol(Vector2f position, float layer, float reloadTime, int currentMagazine, int ammo, int roundsPerMinute, int uniqueId) {
        super(ResourceHandler.pistolTexture, ResourceHandler.pistolTexture, position, layer, uniqueId);

        this.reloadTime = reloadTime;
        this.magazine = currentMagazine;
        this.ammo = ammo;
        this.roundsPerMinute = roundsPerMinute;
        this.position = position;
        this.isAutomatic = false;
        this.isReloading = false;
        this.isFiring = false;
        this.spawnBullet = false;
        this.displayName = "Peestol";

        this.mesh = ResourceHandler.pistolQuad;
    }
}
