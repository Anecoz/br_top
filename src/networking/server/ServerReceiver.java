package networking.server;

import networking.server.serverlogic.ServerPickupItem;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Handles all receiving done after it passes through the entry point in the main application
// The server handles state by means of the connections (accessible by getConnections() on the server object)
// The world representation (with pickups and whatnot) is handled right here in this very class
public class ServerReceiver {

    private static HashMap<Vector2i, List<ServerPickupItem>> droppedItems;

    public static void init() {
        droppedItems = new HashMap<>();
    }

    public synchronized static void addItemToWorld(ServerPickupItem item) {
        // First check if there already is an item at that position
        Vector2i position = new Vector2i((int)item.position.x, (int)item.position.y);
        if (!droppedItems.containsKey(position))
            droppedItems.put(position, new ArrayList<>());

        droppedItems.get(position).add(item);
    }

    // Returns true if we in fact could remove an item from the given position, false if we couldn't (for any reason)
    public synchronized static boolean removeItemFromWorld(Vector2i position, int uniqueId) {
        if (droppedItems.containsKey(position)) {
            // Don't forget to remove from the list
            ServerPickupItem removedItem = null;
            List<ServerPickupItem> list = droppedItems.get(position);
            for (ServerPickupItem item : list) {
                if (item.uniqueId == uniqueId) {
                    removedItem = item;
                    break;
                }
            }
            if (removedItem != null) {
                list.remove(removedItem);
                if (list.size() == 0)
                    droppedItems.remove(position);
                return true;
            }
        }
        return false;
    }

    // Checks if there is an item with a particular id at given position, and in that case grants it
    public synchronized static void handlePickupRequest(GameConnection conn, Vector2i position, int uniqueId) {
        if (droppedItems.containsKey(position)) {
            List<ServerPickupItem> list = droppedItems.get(position);
            for (ServerPickupItem item : list) {
                if (item.uniqueId == uniqueId) {
                    // Remove from world and send rebroadcast that this item is now removed from the world
                    if (!removeItemFromWorld(position, uniqueId))
                        System.err.println("Found item to pickup but got error when trying to remove, pos: " + position.x + ", " + position.y + " and id: " + uniqueId);
                    ServerSender.removeItemFromClient(conn, position, uniqueId);
                    ServerSender.acceptPickup(conn, position, uniqueId);
                    break;
                }
            }
        }
    }

}
