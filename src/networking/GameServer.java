package networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.HashMap;

import static networking.Network.*;

// The game server
public class GameServer {

    //private HashMap<Vector2f, Integer> playerMap;   // Keeps track of positions and
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
                    System.out.println("Someone trying to register to server");
                    // Check if already registered
                    if (connection.id != -1)
                        return;

                    // If not, go!
                    RegisterToServer reg = (RegisterToServer)object;
                    connection.id = counter;
                    counter++;
                    sendNewRegister(connection.id, reg.initPos, reg.displayName, connection);
                }
                else if (object instanceof UpdatePlayerPosition) {
                    // Is something fucky?
                    if (connection.id == -1)
                        return;
                    // Nah, we're good, get the id
                    int playerId = connection.id;

                    UpdatePlayerPosition up = (UpdatePlayerPosition) object;
                    //System.out.println("Update pos received, id is: " + playerId + " and pos: " + up.pos.x + ", " + up.pos.y);
                    sendNewPosition(playerId, up.pos, connection);
                }

            }

            public void disconnected(Connection c) {
                GameConnection connection = (GameConnection) c;
                System.out.println("Someone disconnected from server :(");
                // TODO: ANnounce that a player has left and update client accordingly
            }
        });

        try {
            server.bind(Network.port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
        System.out.println("Server started");
    }

    private void sendNewPosition(int id, Vector2f pos, GameConnection connection) {
        UpdatePosition up = new UpdatePosition();
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
    private static class GameConnection extends Connection {
        public int id = -1;
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
