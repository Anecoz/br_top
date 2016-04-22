package logic;

import audio.AudioMaster;
import audio.AudioSource;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import graphics.Camera;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import graphics.shadows.ShadowTexture;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import utils.ResourceHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameState {

    public static enum GameStates{
        GAME_START, GAME_LOBBY, GAME_INIT, GAME_RUNNING, GAME_OVER, GAME_END,
        GAME_EXIT
    }

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

    public static GameStates gameState;

    public GameState(){
        gameState = GameStates.GAME_START;
        shaderHandler = new ShaderHandler();
        resourceHandler = new ResourceHandler();
        shadowHandler = new ShadowHandler();
    }

    public GameStates getGameState(){
        return gameState;
    }

    public void update(){
        switch (gameState){
            case GAME_START:                            // Only run once! Initialize opengl and lwjgl.
                glInit();                               // And menu.
                menuInit();
                gameState = GameStates.GAME_LOBBY;
                break;
            case GAME_LOBBY:                            // Main Menu loop.
                loop();
                break;
            case GAME_INIT:                             // Only run once! Initialize a game.
                gameInit();
                gameState = GameStates.GAME_RUNNING;
                break;
            case GAME_RUNNING:                          // Game running loop.
                loop();
                break;
            case GAME_OVER:                             // Game is over.
                gameCleanUp();
                loop();
                break;
            case GAME_END:                              // Exit program.
                exitCleanUp();
                break;
        }

        System.out.println(gameState);
    }

    private void glInit() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SAMPLES, 8);    // TODO: Make an option

        window = glfwCreateWindow(WIDTH, HEIGHT, "BR Top", NULL, NULL);
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
        glEnable(GL_MULTISAMPLE);
        System.out.println("Vendor: " + glGetString(GL_VENDOR));
        System.out.println("Renderer: " + glGetString(GL_RENDERER));
        System.out.println("Version: " + glGetString(GL_VERSION));
    }

    private void menuInit(){
        TextMaster.init();
        AudioMaster.init();
        resourceHandler.init();
        shaderHandler.init();

        GUIText text = new GUIText("Welcome to Kapperino Kapperoni... Kappa", 4, ResourceHandler.font, new Vector2f(0f, 0f), 1f, true);
        text.setColour(1f, 1f, 1f);
        text.setRenderParams(0.3f, 0.3f, 0.5f, 0.2f);
    }

    private void gameInit() {
        cam = new Camera(WIDTH, HEIGHT);
        level = new Level("maps/map_01.tmx");
        projMatrix = cam.getProjection();
        shadowTexture = shadowHandler.calcShadowTexture(level);
        player = new Player();
        AudioMaster.setListenerData(player.getPosition(),new Vector2f(player.getSpeed(), player.getSpeed()));
        ambienceSound = new AudioSource();
        ambienceSound.setPosition(player.getPosition());
        ambienceSound.setLooping(true);
        ambienceSound.setVolume(1);
        ambienceSound.play(ResourceHandler.ambienceSoundBuffer);
    }

    private void loop() {
        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        if(gameState == GameStates.GAME_LOBBY || gameState == GameStates.GAME_OVER) {
            // DO an initial update
            // TODO: Add menu logic update here.
            while ((gameState == GameStates.GAME_LOBBY && glfwWindowShouldClose(window) == GLFW_FALSE) ||
                    (gameState == GameStates.GAME_OVER && glfwWindowShouldClose(window) == GLFW_FALSE)) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1.0) {
                    // TODO: Add menu logic update here.
                    glfwPollEvents();
                    updates++;
                    delta--;
                }
                renderMenu();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    glfwSetWindowTitle(window, updates + " ups, " + frames + " fps");
                    updates = 0;
                    frames = 0;
                }
                //TEMP//////////////////////////
                if (KeyInput.isKeyDown(GLFW_KEY_SPACE) && gameState == GameStates.GAME_LOBBY) {
                    gameState = GameStates.GAME_INIT;
                }
                if (KeyInput.isKeyDown(GLFW_KEY_ESCAPE) || glfwWindowShouldClose(window) == GLFW_TRUE) {
                    gameState = GameStates.GAME_END;
                }
                ////////////////////////////////
            }
        }
        else if(gameState == GameStates.GAME_RUNNING) {
            // DO an initial update
            updateLogic();
            while (gameState == GameStates.GAME_RUNNING && glfwWindowShouldClose(window) == GLFW_FALSE) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1.0) {
                    updateLogic();
                    updates++;
                    delta--;
                }
                renderGame();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    glfwSetWindowTitle(window, updates + " ups, " + frames + " fps");
                    updates = 0;
                    frames = 0;
                }
                //TEMP//////////////////////////
                if (KeyInput.isKeyDown(GLFW_KEY_O)) {
                    gameState = GameStates.GAME_OVER;
                }
                if (KeyInput.isKeyDown(GLFW_KEY_ESCAPE) || glfwWindowShouldClose(window) == GLFW_TRUE) {
                    gameState = GameStates.GAME_END;
                }
                ////////////////////////////////
            }
        }
        else {
            // Normal Update?
        }
    }

    private void updateLogic() {
        glfwPollEvents();
        cam.update(player.getPosition(), player.getSpeed(), level);
        player.update(level, projMatrix);
        projMatrix = cam.getProjection();
    }

    private void renderGame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        level.render(projMatrix, shadowTexture, player);
        player.render(projMatrix);

        TextMaster.render();

        glfwSwapBuffers(window);
    }

    private void renderMenu(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        TextMaster.render();
        // Add menu renders

        glfwSwapBuffers(window);
    }

    private void gameCleanUp(){
        if(ambienceSound != null)
            ambienceSound.delete();
        if(player != null)
            player.cleanUp();
        if(level != null)
            level.cleanUp();
    }

    private void exitCleanUp(){
        gameState = GameStates.GAME_EXIT;
        gameCleanUp();
        AudioMaster.cleanUp();
        if(resourceHandler != null)
            resourceHandler.cleanUp();
        if(shadowTexture != null)
            shadowTexture.cleanUp();
        if(shaderHandler != null)
            shaderHandler.cleanUp();
        TextMaster.cleanUp();
        glfwDestroyWindow(window);
        errorCallback.release();
    }
}
