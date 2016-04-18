package logic.weapons;

import graphics.lowlevel.Texture;
import org.joml.Vector2f;

public class Pistol extends Weapon{

    public Pistol(Texture sprite, Vector2f position, float reloadTime, int magazineSize, int roundsPerMinute) {
        super(sprite, position);

        this.reloadTime = reloadTime;
        this.magazineSize = magazineSize;
        this.magazine = magazineSize;
        this.ammo = magazineSize;
        this.roundsPerMinute = roundsPerMinute;
    }
}
