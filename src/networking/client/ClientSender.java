package networking.client;

import org.joml.Vector2f;

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
        //disc.displayName =
        client.sendTCP(disc);
    }
}
