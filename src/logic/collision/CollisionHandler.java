package logic.collision;


import logic.Level;
import logic.Player;
import org.joml.Vector2f;

import java.awt.*;

public class CollisionHandler {
    public CollisionHandler() {

    }

    // Returns what factor you have to multiply incoming velocity by to get to collision
    public static float sweptAABBCollision(CollisionBox moving, Rectangle b2) {
        float xInvEntry, yInvEntry;
        float xInvExit, yInvExit;

        // find the distance between the objects on the near and far sides for both x and y
        if (moving.vel.x > 0.0f)
        {
            xInvEntry = b2.x - (moving.x + moving.w);
            xInvExit = (b2.x + b2.width) - moving.x;
        }
        else
        {
            xInvEntry = (b2.x + b2.width) - moving.x;
            xInvExit = b2.x - (moving.x + moving.w);
        }
        if (moving.vel.y > 0.0f)
        {
            yInvEntry = b2.y - (moving.y + moving.h);
            yInvExit = (b2.y + b2.height) - moving.y;
        }
        else
        {
            yInvEntry = (b2.y + b2.height) - moving.y;
            yInvExit = b2.y - (moving.y + moving.h);
        }

        // find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
        float xEntry, yEntry;
        float xExit, yExit;

        if (moving.vel.x == 0.0f)
        {
            xEntry = -999999999.0f;
            xExit = 999999999.0f;
        }
        else
        {
            xEntry = xInvEntry / moving.vel.x;
            xExit = xInvExit / moving.vel.x;
        }

        if (moving.vel.y == 0.0f)
        {
            yEntry = -999999999.0f;
            yExit = 999999999.0f;
        }
        else
        {
            yEntry = yInvEntry / moving.vel.y;
            yExit = yInvExit / moving.vel.y;
        }

        // find the earliest/latest times of collision
        float entryTime = Math.max(xEntry, yEntry);
        float exitTime = Math.min(xExit, yExit);

        // if there was no collision
        if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f || xEntry > 1.0f || yEntry > 1.0f)
        {
            //normal.x = 0.0f;
            //normal.y = 0.0f;
            return 1.0f;
        }
        else // if there was a collision
        {
            // calculate normal of collided surface
            /*if (xEntry > yEntry)
            {
                if (xInvEntry < 0.0f)
                {
                    normalx = 1.0f;
                    normaly = 0.0f;
                }
                else
                {
                    normalx = -1.0f;
                    normaly = 0.0f;
                }
            }
            else
            {
                if (yInvEntry < 0.0f)
                {
                    normalx = 0.0f;
                    normaly = 1.0f;
                }
                else
                {
                    normalx = 0.0f;
                    normaly = -1.0f;
                }
            }*/
            // return the time of collision
            return entryTime;
        }
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
