package networking.server;

// Handles all functionality regarding stuff that needs to be sent from server to clients

import com.esotericsoftware.kryonet.Connection;
import networking.server.serverlogic.ServerPickupItem;
import networking.server.serverlogic.ServerWeapon;
import networking.shared.Network;
import org.joml.Vector2f;

import static networking.server.GameServer.server;
import static networking.shared.Network.*;

public class ServerSender {

    // Tells clients that a new player has joined and to register that player
    public static void sendNewRegister(int id, Vector2f pos, String displayName, GameConnection connection) {
        RegisterOtherPlayer reg = new RegisterOtherPlayer();
        reg.displayName = displayName;
        reg.id = id;
        reg.pos = pos;

        server.sendToAllExceptTCP(connection.getID(), reg);
    }

    // Tells clients to update a specific player's (identified by id) position
    public static void sendNewPosition(int id, Vector2f pos, GameConnection connection) {
        UpdateOtherPlayerPosition up = new UpdateOtherPlayerPosition();
        up.id = id;
        up.pos = pos;

        server.sendToAllExceptTCP(connection.getID(), up);
    }

    // Tells clients to update a specific player's (identified by id) forward vector
    public static void sendNewForward(int id, Vector2f forward, GameConnection connection) {
        UpdateOtherPlayerForward up = new UpdateOtherPlayerForward();
        up.id = id;
        up.forward = forward;

        server.sendToAllExceptTCP(connection.getID(), up);
    }

    // Sends all currently registered player's to __ONE__ client (upon joining a game for instance), so that he is
    // up to date
    public static void sendAllPlayers(GameConnection playerConn) {
        RegisterCurrentOtherPlayers reg = new RegisterCurrentOtherPlayers();

        Connection[] connections = server.getConnections();
        if (connections.length != 1) {
            int[] ids = new int[connections.length - 1];
            String[] names = new String[connections.length - 1];
            Vector2f[] positions = new Vector2f[connections.length - 1];

            int counter = 0;
            for (int i = 0; i < connections.length; i++) {
                GameConnection connection = (GameConnection)connections[i];
                if (connection.id != playerConn.id) {
                    ids[counter] = connection.id;
                    names[counter] = connection.displayName;
                    positions[counter] = connection.pos;
                    counter++;
                }
            }

            reg.ids = ids;
            reg.positions = positions;
            reg.displayNames = names;
            server.sendToTCP(playerConn.getID(), reg);
        }
    }

    // Notifies all (except the disconnected player) current players that another player has disconnected from the server
    public static void disconnectPlayer(GameConnection connection) {
        OtherPlayerDisconnect disc = new OtherPlayerDisconnect();
        disc.id = connection.id;
        server.sendToAllExceptTCP(connection.getID(), disc);
    }

    // Notify everyone (except the connection) of a new added world item
    public static void sendNewWorldItem(GameConnection connection, ServerPickupItem item) {
        AddItemToClient req = new AddItemToClient();
        if (item.type == ITEM_TYPES.ASSAULT_RIFLE || item.type == ITEM_TYPES.PISTOL) {
            ServerWeapon wep = (ServerWeapon) item;
            req.ammo = wep.ammo;
            req.magazine = wep.magazine;
            req.position = wep.position;
        }
        req.type = item.type;
        server.sendToAllExceptTCP(connection.getID(), req);
    }
}
