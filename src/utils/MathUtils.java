package utils;

import graphics.Camera;
import logic.GameState;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathUtils {

    // Takes mouse pixel coordinates and gives the world coordinates back. It's really cool
    public static Vector2f screenSpaceToWorld(Vector2f screenPoint, int screenWidth, int screenHeight, Matrix4f viewProj) {
        double x = 2.0 * (double)screenPoint.x / (double) screenWidth - 1.0;
        double y = -2.0 * (double)screenPoint.y / (double) screenHeight + 1.0;
        Matrix4f invProj = viewProj.invert();

        Vector3f out = new Vector3f((float)x, (float)y, 0.0f);
        invProj.transformPosition(out);
        return new Vector2f(out.x, out.y);
    }

    // Takes mouse screen coordinates and gives the corresponding GUI coordinates back
    // GUI coords: x[0,1], y[0,1] with origin in upper left
    public static Vector2f screenSpaceToGUI(Vector2f screenPoint) {
        float x = screenPoint.x / (float)GameState.WIDTH;
        float y = screenPoint.y / (float)GameState.HEIGHT;
        return new Vector2f(x, y);
    }

    // Returns whether a given mouse coord is within the inventory, specified by the in position
    // Inventorypos is in GUI coordinates, screenpoint is in mouse coordinates
    public static boolean screenPointWithinInventory(Vector2f screenPoint, Vector2f inventoryPos, float baseScale) {
        Vector2f mouseGUI = screenSpaceToGUI(screenPoint);
        // Get inventory dimensions in GUI coordinates
        float inventoryWidth = baseScale/ Camera.getWinSizeX();
        float inventoryHeight = baseScale/ Camera.getWinSizeY();

        boolean out = false;

        if (mouseGUI.x <= inventoryPos.x + inventoryWidth && mouseGUI.x >= inventoryPos.x) {
            if (mouseGUI.y <= inventoryPos.y + inventoryHeight && mouseGUI.y >= inventoryPos.y)
                out = true;
        }

        return out;
    }

    public static int getInventoryItemIndex(Vector2f mousePos, Vector2f position, float baseScale, int itemPerRow) {
        Vector2f mFix = MathUtils.screenSpaceToGUI(mousePos).sub(position);
        double itemWidth = (double)baseScale/(double)(Camera.getWinSizeX()*(double)itemPerRow);
        double itemHeight = (double)baseScale/(double)(Camera.getWinSizeY()*(double)itemPerRow);
        int indX = (int)Math.floor((double)mFix.x/itemWidth);
        int indY = (int)Math.floor((double)mFix.y/itemHeight);

        return indX + itemPerRow*indY;
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min)
            return min;
        else if (val >= max)
            return max;
        else
            return val;
    }
}
