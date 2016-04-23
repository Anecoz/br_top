package input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosInput extends GLFWCursorPosCallback {

    private static double x;
    private static double y;

    public static double getX() {return x;}
    public static double getY() {return y;}

    public static Vector2f getPosition() {
        return new Vector2f((float)x, (float)y);
    }

    @Override
    public void invoke(long window, double xPos, double yPos) {
        x = xPos;
        y = yPos;
    }
}
