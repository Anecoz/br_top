package networking.client;

import logic.Level;
import logic.OtherPlayer;
import logic.Player;
import logic.inventory.InventoryItem;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

// Used when receiving stuff from the server, to improve abstraction
// Holds state information about all other players
public class ClientReceiver {
    private static List<OtherPlayer> playerList;

    public static void init() {
        playerList = new ArrayList<>();
        ClientMasterHandler.init();
    }

    public static void addItemToWorld(InventoryItem item) {
        Level.addPickupToQueue(item);
    }

    public static void addAllCurrentOtherPlayers(int[] ids, String[] names, Vector2f[] positions) {
        for (int i = 0; i < ids.length; i++) {
            addOtherPlayer(ids[i], positions[i], names[i]);
        }
    }

    public static void disconnectOtherPlayer(int id) {
        for (OtherPlayer player : playerList) {
            if (player.getId() == id) {
                playerList.remove(player);
                break;
            }
        }
    }

    public static void updateOtherPlayerForward(Vector2f forward, int id) {
        for (OtherPlayer player : playerList) {
            if (player.getId() == id) {
                player.setForward(forward);
            }
        }
    }

    public static void updateOtherPlayerPosition(Vector2f pos, int id) {
        for (OtherPlayer player : playerList) {
            if (player.getId() == id) {
                player.setPosition(pos);
            }
        }
    }

    public static void addOtherPlayer(int id, Vector2f pos, String displayName) {
        playerList.add(new OtherPlayer(pos, id, displayName));
    }

    public static void addPickupSuccess(Vector2i position, int uniqueId) {
        Player.addPickupRequest(position, uniqueId);
    }

    public static void removeItemFromWorld(Vector2i position, int uniqueId) {
        Level.addItemToRemoveToQueue(position, uniqueId);
    }

    public static void render(Matrix4f projection) {
        for (OtherPlayer player : playerList) {
            player.render(projection);
        }
    }
}
