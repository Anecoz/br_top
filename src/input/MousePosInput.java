package input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosInput extends GLFWCursorPosCallback {

    private static double x;
    private static double y;

    public static double getX() {return x;}
    public static double getY() {return y;}

    @Override
    public void invoke(long window, double xPos, double yPos) {
        x = xPos;
        y = yPos;
    }
}
