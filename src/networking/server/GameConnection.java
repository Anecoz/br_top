package networking.server;

import com.esotericsoftware.kryonet.Connection;
import org.joml.Vector2f;

// A modified version of the connection, that let's us keep track of what player is associated with what connection
// Basically represents a player on the server
public class GameConnection extends Connection {
    public int id = -1;
    public Vector2f pos;
    public String displayName;
}
