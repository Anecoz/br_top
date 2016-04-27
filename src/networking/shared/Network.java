package networking.shared;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.List;

// Class that holds all shared object types that are to be sent over the network. Keep them simple!
public class Network {
    static public final int port = 54557;

    public enum ITEM_TYPES {
        ASSAULT_RIFLE, PISTOL, BULLET, UNKNOWN
    }

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(ITEM_TYPES.class);
        kryo.register(Vector2f.class);
        kryo.register(RegisterPlayerToServer.class);
        kryo.register(DisconnectPlayer.class);
        kryo.register(AddItemToServer.class);
        kryo.register(Vector2i.class);
        kryo.register(ItemPickupRequest.class);
        kryo.register(ItemPickupSuccess.class);
        kryo.register(RemoveItemFromClient.class);
        kryo.register(AddItemToClient.class);
        kryo.register(RegisterOtherPlayer.class);
        kryo.register(int[].class);
        kryo.register(Vector2f[].class);
        kryo.register(String[].class);
        kryo.register(RegisterCurrentOtherPlayers.class);
        kryo.register(UpdatePlayerPosition.class);
        kryo.register(UpdateOtherPlayerPosition.class);
        kryo.register(OtherPlayerDisconnect.class);
        kryo.register(UpdatePlayerForward.class);
        kryo.register(UpdateOtherPlayerForward.class);
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends this to server to update own forward
    static public class UpdatePlayerForward {
        public Vector2f forward;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Tells the server about our new position
    static public class UpdatePlayerPosition {
        public Vector2f pos;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends this to server to register. Gets an id on server and is broadcast to all other players
    static public class RegisterPlayerToServer {
        public String displayName;
        public Vector2f initPos;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends this to server to notify that we have disconnected (for instance gone to main menu)
    static public class DisconnectPlayer {
        // We actually don't need anything in here, server will know what player it is by the id of his connection
        public String displayName;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Client sends to notify that an item has been dropped to the world
    static public class AddItemToServer {
        public ITEM_TYPES type;
        public Vector2f position;
        public int uniqueId;
        // if the item is a weapon we need these things
        public int ammo;
        public int magazine;
        // if the item is a ammunition type we need this
        public Vector2f velocity;
    }

    // FROM:    CLIENT
    // TO:      SERVER
    // desc:    Sends a "I would like to pickup an item at this position with this ID"-request to the server
    static public class ItemPickupRequest {
        public Vector2i position;
        public int uniqueId;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to notify client of successfull pickup
    static public class ItemPickupSuccess {
        public Vector2i position;
        public int uniqueId;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Send a client request to remove a specific item from the world
    static public class RemoveItemFromClient {
        public Vector2i position;
        public int uniqueId;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to notify clients of a new world item
    static public class AddItemToClient {
        public ITEM_TYPES type;
        public Vector2f position;
        public int uniqueId;
        // if the item is a weapon we need these things
        public int ammo;
        public int magazine;
        // if the item is a ammunition type we need this
        public Vector2f velocity;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to all other players when a new player should be registered by them
    static public class RegisterOtherPlayer {
        public int id;
        public Vector2f pos;
        public String displayName;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Server sends this to a newly connected player, so that he gets all current ones aswell
    static public class RegisterCurrentOtherPlayers {
        public int[] ids;
        public Vector2f[] positions;
        public String[] displayNames;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent to all (but yourself) to update other positions.
    static public class UpdateOtherPlayerPosition {
        public Vector2f pos;
        public int id;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent to all (but yourself) to update some other players forward.
    static public class UpdateOtherPlayerForward {
        public Vector2f forward;
        public int id;
    }

    // FROM:    SERVER
    // TO:      CLIENT
    // desc:    Sent whenever a player has disconnected from the server
    static public class OtherPlayerDisconnect {
        public int id;
    }
}
