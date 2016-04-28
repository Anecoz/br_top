package networking.client;

import logic.inventory.InventoryItem;
import logic.weapons.Ammunition;
import logic.weapons.Bullet;
import logic.weapons.Pistol;
import logic.weapons.Weapon;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static networking.client.ClientMasterHandler.client;
import static networking.shared.Network.*;

// Used for sending stuff from the client to the server, to improve abstraction
public class ClientSender {

    public static void registerPlayer(String name, Vector2f pos) {
        RegisterPlayerToServer reg = new RegisterPlayerToServer();
        reg.displayName = name;
        reg.initPos = pos;
        client.sendTCP(reg);
    }

    public static void updatePlayerPos(Vector2f pos) {
        UpdatePlayerPosition up = new UpdatePlayerPosition();
        up.pos = pos;
        client.sendTCP(up);
    }

    public static void updatePlayerForward(Vector2f forward) {
        UpdatePlayerForward up = new UpdatePlayerForward();
        up.forward = forward;
        client.sendTCP(up);
    }

    public static void disconnectPlayer() {
        DisconnectPlayer disc = new DisconnectPlayer();
        client.sendTCP(disc);
    }

    public static void spawnProjectile(Ammunition item) {
        AddItemToServer req = new AddItemToServer();

        if (item instanceof Bullet) {
            req.type = ITEM_TYPES.BULLET;
            req.velocity = item.getVelocity();
            req.damage = item.getDamage();
        }else {
            req.ammo = -1;
            req.magazine = -1;
            req.velocity = new Vector2f(-1);
            req.type = ITEM_TYPES.UNKNOWN;
        }
        req.uniqueId = item.getUniqueId();
        req.position = item.getPosition();
        client.sendTCP(req);
    }

    public static void addItemToWorld(InventoryItem item) {
        AddItemToServer req = new AddItemToServer();

        if (item instanceof Weapon) {
            req.ammo = ((Weapon) item).getAmmo();
            req.magazine = ((Weapon) item).getMagazine();

            if (item instanceof Pistol)
                req.type = ITEM_TYPES.PISTOL;
            else
                req.type = ITEM_TYPES.ASSAULT_RIFLE;
        }
        else {
            req.ammo = -1;
            req.magazine = -1;
            req.velocity = new Vector2f(-1);
            req.type = ITEM_TYPES.UNKNOWN;
        }
        req.uniqueId = item.getUniqueId();
        req.position = item.getPosition();
        client.sendTCP(req);
    }

    public static void sendPickupRequest(InventoryItem item) {
        ItemPickupRequest req = new ItemPickupRequest();
        req.position = new Vector2i((int)item.getPosition().x, (int)item.getPosition().y);
        req.uniqueId = item.getUniqueId();
        client.sendTCP(req);
    }
}
