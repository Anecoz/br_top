package networking.client;

import logic.OtherPlayer;
import org.joml.Matrix4f;
import org.joml.Vector2f;

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

    public static void render(Matrix4f projection) {
        for (OtherPlayer player : playerList) {
            player.render(projection);
        }
    }
}
