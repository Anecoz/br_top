package networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.joml.Vector2f;

import java.util.List;

// Class that holds all shared object types that are to be sent over the network. Keep them simple!
public class Network {
    static public final int port = 54557;

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Vector2f.class);
        kryo.register(RegisterToServer.class);
        kryo.register(RegisterPlayer.class);
        kryo.register(UpdatePlayerPosition.class);
        kryo.register(UpdatePosition.class);
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends this to server to register. Gets an id on server and is broadcast to all other players
    static public class RegisterToServer {
        public String displayName;
        public Vector2f initPos;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to all other players when a new player should be registered by them
    static public class RegisterPlayer {
        public int id;
        public Vector2f pos;
        public String displayName;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Tells the server about our new position
    static public class UpdatePlayerPosition {
        public Vector2f pos;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent to all (but yourself) to update other positions.
    static public class UpdatePosition {
        public Vector2f pos;
        public int id;
    }
}
