package logic;


import org.joml.Vector2f;

public class CollisionHandler {
    public CollisionHandler() {

    }

    public static boolean checkPlayerCollision(Player player, Level level) {
        Vector2f pos = player.getPosition();
        int size = player.getSize();

        // Check all 4 corners of players bounding box
        if (level.getCollAt((int)pos.x, (int)pos.y)) {
            return true;
        }
        else if (level.getCollAt((int)(pos.x + size), (int)pos.y)) {
            return true;
        }
        else if (level.getCollAt((int)pos.x, (int)(pos.y + size))) {
            return true;
        }
        else if (level.getCollAt((int)(pos.x + size), (int)(pos.y + size))) {
            return true;
        }
        else
            return false;
    }
}
