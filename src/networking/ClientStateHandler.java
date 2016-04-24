package networking;

import logic.OtherPlayer;
import logic.Player;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

// Keeps track of things such as other players and making sure we are sending updates over the network
public class ClientStateHandler {
    private static List<OtherPlayer> playerList;

    public static void init() {
        playerList = new ArrayList<>();
        ClientHandler.init();
    }

    /*----------------------THIS PLAYER STUFF----------------*/

    public static void updatePlayerForward(Vector2f forward) {
        ClientHandler.updatePlayerForward(forward);
    }

    public static void updatePlayerPos(Vector2f pos) {
        ClientHandler.updatePlayerPos(pos);
    }

    public static void registerPlayer(Player player, Vector2f initPos) {
        ClientHandler.registerPlayer(player.displayName, initPos);
    }

    /*----------------------OTHER PLAYER STUFF----------------*/

    public static void updateOtherPlayerForward(int id, Vector2f forward) {
        for (OtherPlayer player : playerList) {
            if (player.getId() == id) {
                player.setForward(forward);
            }
        }
    }

    public static void updateOtherPlayerPos(int id, Vector2f pos) {
        for (OtherPlayer player : playerList) {
            if (player.getId() == id) {
                player.setPosition(pos);
            }
        }
    }

    public static void addNewPlayer(int id, Vector2f initPos, String displayName) {
        playerList.add(new OtherPlayer(initPos, id, displayName));
    }

    public static void removePlayer(int id) {
        for (OtherPlayer player : playerList) {
            if (player.getId() == id) {
                playerList.remove(player);
                break;
            }
        }
    }

    public static void render(Matrix4f projection) {
        for (OtherPlayer player : playerList) {
            player.render(projection);
        }
    }
}
