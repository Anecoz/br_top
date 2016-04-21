import logic.GameState;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        System.out.println("Using LWJGL " + Version.getVersion() + "!");
        GameState gameState = new GameState();
        try {
            while(gameState.getGameState() != GameState.GameStates.GAME_EXIT) {
                gameState.update();
            }
        } finally {
            glfwTerminate();
        }
    }
}