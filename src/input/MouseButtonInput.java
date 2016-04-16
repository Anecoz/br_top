package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonInput extends GLFWMouseButtonCallback {

    private static boolean LEFT_DOWN = false;
    private static boolean RIGHT_DOWN = false;

    public static boolean isLeftDown() {return LEFT_DOWN;}

    public static boolean isRightDown() {return RIGHT_DOWN;}

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            if (action == GLFW.GLFW_PRESS) {
                LEFT_DOWN = true;
            }
            else if (action == GLFW.GLFW_RELEASE) {
                LEFT_DOWN = false;
            }
        }
        else if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
            if (action == GLFW.GLFW_PRESS) {
                RIGHT_DOWN = true;
            }
            else if (action == GLFW.GLFW_RELEASE) {
                RIGHT_DOWN = false;
            }
        }
    }
}
