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

                else if (object instanceof UpdatePosition) {
                    UpdatePosition up = (UpdatePosition) object;
                    updateOtherPlayerPosition(up.pos, up.id);
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Disconnected! :(");
            }
        });

        // For now, just connect to localhost immediately (server must be running of course)
        try {
            client.connect(5000, "localhost", Network.port);
        }
        catch (IOException e ) {
            e.printStackTrace();
        }
    }

    private static void updateOtherPlayerPosition(Vector2f pos, int id) {
        ClientStateHandler.updateOtherPlayerPos(id, pos);
    }

    private static void addNewPlayer(int id, Vector2f pos, String displayName) {
        ClientStateHandler.addNewPlayer(id, pos, displayName);
    }

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
}
