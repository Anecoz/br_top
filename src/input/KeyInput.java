package input;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyInput extends GLFWKeyCallback {

    private static boolean[] keys = new boolean[65536];
    private static List<Integer> pressed = new ArrayList<>();

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
        if(action == GLFW_RELEASE && pressed.contains(key))
            pressed.remove(new Integer(key));
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isKeyClicked(int key){
        if (!keys[key])
            return false;
        if (pressed.contains(key))
            return false;
        pressed.add(key);
        return true;
    }
}
