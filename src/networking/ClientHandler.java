package networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.List;

import static networking.Network.*;

// Statically handles (we only ever need one per game) all network updates for the client
public class ClientHandler {
    private static Client client;

    public static void init() {
        client = new Client();
        client.start();

        // Register all classes that will be sent
        Network.register(client);

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                System.out.println("Connected!");
            }

            public void received(Connection connection, Object object) {
                if (object instanceof RegisterPlayer) {
                    RegisterPlayer reg = (RegisterPlayer) object;
                    addNewPlayer(reg.id, reg.pos, reg.displayName);
                }

                else if (object instanceof UpdateOtherPosition) {
                    UpdateOtherPosition up = (UpdateOtherPosition) object;
                    updateOtherPlayerPosition(up.pos, up.id);
                }

                else if (object instanceof UpdateOtherForward) {
                    UpdateOtherForward up = (UpdateOtherForward) object;
                    updateOtherPlayerForward(up.forward, up.id);
                }

                else if (object instanceof RegisterCurrentPlayers) {
                    RegisterCurrentPlayers reg = (RegisterCurrentPlayers) object;
                    addAllCurrentPlayers(reg.ids, reg.displayNames, reg.positions);
                }

                else if (object instanceof PlayerDisconnect) {
                    PlayerDisconnect disc = (PlayerDisconnect) object;
                    disconnectPlayer(disc.id);
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Disconnected! :(");
            }
        });

        // For now, just connect to localhost immediately (server must be running of course)
        try {
            client.connect(5000, "85.229.139.37", Network.port);
        }
        catch (IOException e ) {
            e.printStackTrace();
        }
    }

    /*----------------------OTHER PLAYER STUFF----------------*/

    private static void disconnectPlayer(int id) {
        ClientStateHandler.removePlayer(id);
    }

    private static void updateOtherPlayerForward(Vector2f forward, int id) {
        ClientStateHandler.updateOtherPlayerForward(id, forward);
    }

    private static void addAllCurrentPlayers(int[] ids, String[] names, Vector2f[] positions) {
        for (int i = 0; i < ids.length; i++) {
            ClientStateHandler.addNewPlayer(ids[i], positions[i], names[i]);
        }
    }

    private static void updateOtherPlayerPosition(Vector2f pos, int id) {
        ClientStateHandler.updateOtherPlayerPos(id, pos);
    }

    private static void addNewPlayer(int id, Vector2f pos, String displayName) {
        ClientStateHandler.addNewPlayer(id, pos, displayName);
    }


    /*----------------------THIS PLAYER STUFF----------------*/
    public static void registerPlayer(String name, Vector2f pos) {
        RegisterToServer reg = new RegisterToServer();
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
}
