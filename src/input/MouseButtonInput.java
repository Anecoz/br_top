package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class MouseButtonInput extends GLFWMouseButtonCallback {

    private static boolean LEFT_DOWN = false;
    private static boolean RIGHT_DOWN = false;

    private static List<Integer> pressed = new ArrayList<>();
    private static boolean[] buttons = new boolean[128];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttons[button] = action != GLFW_RELEASE;
        if(action == GLFW_RELEASE && pressed.contains(button))
            pressed.remove(new Integer(button));

        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            if (action == GLFW_PRESS) {
                LEFT_DOWN = true;
            } else if (action == GLFW.GLFW_RELEASE) {
                LEFT_DOWN = false;
            }
        }
        else if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
            if (action == GLFW_PRESS) {
                RIGHT_DOWN = true;
            } else if (action == GLFW.GLFW_RELEASE) {
                RIGHT_DOWN = false;
            }
        }
    }

    public static boolean isLeftDown() {return LEFT_DOWN;}
    public static boolean isRightDown() {return RIGHT_DOWN;}

    public static boolean isMouseButtonClicked(int button) {
        if (!buttons[button])
            return false;
        if (pressed.contains(button))
            return false;
        pressed.add(button);
        return true;
    }
}
