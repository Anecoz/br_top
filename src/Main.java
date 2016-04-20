import audio.AudioMaster;
import audio.AudioSource;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import graphics.Camera;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import graphics.shadows.ShadowTexture;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.GameState;
import logic.Level;
import logic.Player;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.joml.*;
import utils.FileUtils;
import utils.ResourceHandler;


import java.io.File;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private GLFWErrorCallback errorCallback;
    private static KeyInput keyInput;
    private static MouseButtonInput mouseButtonInput;
    private static MousePosInput mousePosInput;
    private static final int VSYNC = 1;

    private long window;
    private Matrix4f projMatrix;
    private Level level;
    private Camera cam;
    private Player player;
    private ShadowHandler shadowHandler;
    private ShadowTexture shadowTexture;
    private AudioSource ambienceSound;
    private ShaderHandler shaderHandler;
    private ResourceHandler resourceHandler;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static void main(String[] args) {
        new Main().run();
    }

    /*public void run() {
        System.out.println("Using LWJGL " + Version.getVersion() + "!");

        try {
            glInit();
            gameInit();
            loop();
            cleanUp();

            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }*/

    public void run() {
        System.out.println("Using LWJGL " + Version.getVersion() + "!");
        GameState gameState = new GameState();
        try {
            while(gameState.getGameState() != GameState.GameStates.END) {
                gameState.update();
            }
        } finally {
            glfwTerminate();
            //errorCallback.release();
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

        window = glfwCreateWindow(WIDTH, HEIGHT, "3D Game Engine", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, keyInput = new KeyInput());
        glfwSetMouseButtonCallback(window, mouseButtonInput = new MouseButtonInput());
        glfwSetCursorPosCallback(window, mousePosInput = new MousePosInput());

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(VSYNC);
        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        System.out.println("Vendor: " + glGetString(GL_VENDOR));
        System.out.println("Renderer: " + glGetString(GL_RENDERER));
        System.out.println("Version: " + glGetString(GL_VERSION));
    }

    private void gameInit() {
        shaderHandler = new ShaderHandler();
        shaderHandler.init();
        AudioMaster.init();
        resourceHandler = new ResourceHandler();
        resourceHandler.init();
        cam = new Camera(WIDTH, HEIGHT);
        level = new Level("maps/map_01.tmx");
        projMatrix = cam.getProjection();
        shadowHandler = new ShadowHandler();
        shadowTexture = shadowHandler.calcShadowTexture(level);
        player = new Player();
        AudioMaster.setListenerData(player.getPosition(),new Vector2f(player.getSpeed(), player.getSpeed()));
        ambienceSound = new AudioSource();
        ambienceSound.setPosition(player.getPosition());
        ambienceSound.setLooping(true);
        ambienceSound.setVolume(1);
        ambienceSound.play(ResourceHandler.ambienceSoundBuffer);

        // FONTS
        TextMaster.init();

        GUIText text = new GUIText("Welcome to Kapperino Kapperoni... Kappa", 4, ResourceHandler.font, new Vector2f(0f, 0f), 1f, true);
        text.setColour(1f, 1f, 1f);
        text.setRenderParams(0.3f, 0.3f, 0.5f, 0.2f);
    }

    private void loop() {
        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        // DO an initial update
        update();
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
        player.update(level, projMatrix);
        cam.update(player.getPosition(), player.getSpeed(), level);
        projMatrix = cam.getProjection();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        level.render(projMatrix, shadowTexture, player);
        player.render(projMatrix);

        TextMaster.render();

        glfwSwapBuffers(window);
    }

    private void cleanUp(){
        ambienceSound.delete();
        AudioMaster.cleanUp();
        player.cleanUp();
        resourceHandler.cleanUp();
        shadowTexture.cleanUp();
        shaderHandler.cleanUp();
        TextMaster.cleanUp();
    }
}