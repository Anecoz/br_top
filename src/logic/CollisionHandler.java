package logic;


import org.joml.Vector2f;

public class CollisionHandler {
    public CollisionHandler() {

    }

    public static boolean checkPlayerCollision(Player player, Level level) {
        Vector2f playerPos = player.getPosition();
        Vector2f pos = new Vector2f(playerPos);
        // Halve twice to get a smaller bounding box (feels better Kappa)
        float w = player.getWidth()/2.0f;
        float h = player.getHeight()/2.0f;
        pos.x = pos.x + w/2.0f;
        pos.y = pos.y + h/2.0f;

        // Check all 4 corners of players bounding box
        if (level.getCollAt((int)pos.x, (int)pos.y)) {
            return true;
        }
        else if (level.getCollAt((int)(pos.x + w), (int)pos.y)) {
            return true;
        }
        else if (level.getCollAt((int)pos.x, (int)(pos.y + h))) {
            return true;
        }
        else if (level.getCollAt((int)(pos.x + w), (int)(pos.y + h))) {
            return true;
        }
        else
            return false;
    }
}
