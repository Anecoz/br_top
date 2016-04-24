package networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static networking.Network.*;

// The game server
public class GameServer {

    private int counter = 0;                        // Increment this for each new connection (for now at least)
    private Server server;

    private GameServer() {
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };

        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                // Since all connections to the server will be of this type anyway
                GameConnection connection = (GameConnection)c;

                if (object instanceof RegisterToServer) {
                    // Check if already registered
                    if (connection.id != -1)
                        return;

                    // If not, go!
                    RegisterToServer reg = (RegisterToServer)object;
                    connection.id = counter;
                    connection.pos = reg.initPos;
                    connection.displayName = reg.displayName;
                    counter++;
                    sendNewRegister(connection.id, reg.initPos, reg.displayName, connection);
                    // Also notify of all current other players
                    sendAllPlayers(connection);
                }
                else if (object instanceof UpdatePlayerPosition) {
                    // Is something fucky?
                    if (connection.id == -1)
                        return;
                    // Nah, we're good, get the id
                    int playerId = connection.id;

                    UpdatePlayerPosition up = (UpdatePlayerPosition) object;
                    sendNewPosition(playerId, up.pos, connection);
                }

                else if (object instanceof UpdatePlayerForward) {
                    if (connection.id == -1)
                        return;
                    UpdatePlayerForward up = (UpdatePlayerForward) object;
                    sendNewForward(connection.id, up.forward, connection);
                }

            }

            public void disconnected(Connection c) {
                GameConnection connection = (GameConnection) c;
                disconnectPlayer(connection);
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

    private void disconnectPlayer(GameConnection connection) {
        PlayerDisconnect disc = new PlayerDisconnect();
        disc.id = connection.id;
        server.sendToAllExceptTCP(connection.getID(), disc);
    }

    private void sendAllPlayers(GameConnection playerConn) {
        RegisterCurrentPlayers reg = new RegisterCurrentPlayers();

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

    private void sendNewForward(int id, Vector2f forward, GameConnection connection) {
        UpdateOtherForward up = new UpdateOtherForward();
        up.id = id;
        up.forward = forward;

        server.sendToAllExceptTCP(connection.getID(), up);
    }

    private void sendNewPosition(int id, Vector2f pos, GameConnection connection) {
        UpdateOtherPosition up = new UpdateOtherPosition();
        up.id = id;
        up.pos = pos;

        server.sendToAllExceptTCP(connection.getID(), up);
    }

    private void sendNewRegister(int id, Vector2f pos, String displayName, GameConnection connection) {
        RegisterPlayer reg = new RegisterPlayer();
        reg.displayName = displayName;
        reg.id = id;
        reg.pos = pos;

        server.sendToAllExceptTCP(connection.getID(), reg);
    }

    // By using our own connection, we can keep track of the player connections
    // Basically represents a player on the server
    private static class GameConnection extends Connection {
        public int id = -1;
        public Vector2f pos;
        public String displayName;
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
