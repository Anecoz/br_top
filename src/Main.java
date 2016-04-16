import graphics.Camera;
import input.Input;
import logic.Level;
import logic.Player;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private GLFWErrorCallback errorCallback;
    private static Input input;

    private long window;
    private Matrix4f projMatrix;
    private Level level;
    private Camera cam;
    private Player player;

    public void run() {
        System.out.println("Using LWJGL " + Version.getVersion() + "!");

        try {
            glInit();
            gameInit();
            loop();

            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void glInit() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        int WIDTH = 1280;
        int HEIGHT = 720;

        window = glfwCreateWindow(WIDTH, HEIGHT, "3D Game Engine", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, input = new Input());

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        System.out.println("Vendor: " + glGetString(GL_VENDOR));
        System.out.println("Renderer: " + glGetString(GL_RENDERER));
        System.out.println("Version: " + glGetString(GL_VERSION));
    }

    private void gameInit() {
        cam = new Camera();
        projMatrix = cam.getProjection();
        level = new Level("testMap.tmx");
        player = new Player("player.png");
    }

    private void loop() {
        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                glfwSetWindowTitle(window, updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
    }

    private void update() {
        glfwPollEvents();
        player.update(cam, level);
        cam.update();
        projMatrix = cam.getProjection();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        level.render(projMatrix);
        player.render(projMatrix);

        glfwSwapBuffers(window);
    }

    public static void main(String[] args) {
        new Main().run();
    }

}