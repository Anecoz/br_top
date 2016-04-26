package networking.server.serverlogic;

import org.joml.Vector2f;
import static networking.shared.Network.*;

// Representation of a weapon lying on the ground (we need this so that we can keep state like ammo and stuff
// for when players pick it up again)
public class ServerWeapon extends ServerPickupItem {
    public int magazine;
    public int ammo;

    public ServerWeapon(Vector2f position, int magazine, int ammo, ITEM_TYPES type, int uniqueId) {
        super(position, type, uniqueId);
        this.magazine = magazine;
        this.ammo = ammo;
    }
}
