package logic;


import org.joml.Vector2f;

public class CollisionHandler {
    public CollisionHandler() {

    }

    public static boolean checkPlayerCollision(Player player, Level level) {
        Vector2f playerPos = player.getPosition();
        Vector2f pos = new Vector2f(playerPos);
        float size = player.getSize();
        pos.x = pos.x + size/2.0f;
        pos.y = pos.y + size/2.0f;

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
