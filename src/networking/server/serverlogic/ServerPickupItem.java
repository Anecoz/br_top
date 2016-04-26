package networking.server.serverlogic;

import org.joml.Vector2f;

import static networking.shared.Network.*;

// We need some way for items that are on the floor to have state, this class let's us do that
public class ServerPickupItem {
    public Vector2f position;
    public ITEM_TYPES type;
    public int uniqueId;

    public ServerPickupItem(Vector2f position, ITEM_TYPES type, int uniqueId) {
        this.position = position;
        this.type = type;
        this.uniqueId = uniqueId;
    }
}
