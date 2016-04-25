package networking.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import networking.server.serverlogic.ServerPickupItem;
import networking.server.serverlogic.ServerWeapon;
import networking.shared.Network;
import org.joml.Vector2f;

import java.io.IOException;

import static networking.shared.Network.*;

// The game server
public class GameServer {

    private int counter = 0;                        // Increment this for each new connection (for now at least)
    public static Server server;

    private GameServer() {
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };

        ServerReceiver.init();
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                // Since all connections to the server will be of this type anyway
                GameConnection connection = (GameConnection)c;

                if (object instanceof RegisterPlayerToServer) {
                    // Check if already registered
                    if (connection.id != -1)
                        return;

                    // If not, go!
                    RegisterPlayerToServer reg = (RegisterPlayerToServer)object;
                    connection.id = counter;
                    connection.pos = reg.initPos;
                    connection.displayName = reg.displayName;
                    counter++;
                    ServerSender.sendNewRegister(connection.id, reg.initPos, reg.displayName, connection);
                    // Also notify of all current other players
                    ServerSender.sendAllPlayers(connection);
                }
                else if (object instanceof UpdatePlayerPosition) {
                    // Is something fucky?
                    if (connection.id == -1)
                        return;
                    // Nah, we're good, get the id
                    int playerId = connection.id;

                    UpdatePlayerPosition up = (UpdatePlayerPosition) object;
                    ServerSender.sendNewPosition(playerId, up.pos, connection);
                }

                else if (object instanceof UpdatePlayerForward) {
                    if (connection.id == -1)
                        return;
                    UpdatePlayerForward up = (UpdatePlayerForward) object;
                    ServerSender.sendNewForward(connection.id, up.forward, connection);
                }

                else if (object instanceof DisconnectPlayer) {
                    if (connection.id == -1)
                        return;
                    ServerSender.disconnectPlayer(connection);
                }

                else if (object instanceof AddItemToServer) {
                    // Add the item to the server
                    if (connection.id == -1)
                        return;
                    AddItemToServer req = (AddItemToServer) object;
                    if (req.type == ITEM_TYPES.ASSAULT_RIFLE || req.type == ITEM_TYPES.PISTOL) {
                        ServerWeapon item = new ServerWeapon(req.position, req.magazine, req.ammo, req.type);
                        ServerReceiver.addItemToWorld(item);
                        // Send out an update to let players know of the new item
                        ServerSender.sendNewWorldItem(connection, item);
                    }
                }
            }

            public void disconnected(Connection c) {
                GameConnection connection = (GameConnection) c;
                ServerSender.disconnectPlayer(connection);
            }
        });

        try {
            server.bind(Network.port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
