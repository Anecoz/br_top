package logic;

import audio.AudioMaster;
import audio.AudioSource;
import graphics.lighting.LightHandler;
import gui.fontMeshCreator.GUIText;
import gui.fontRendering.TextMaster;
import graphics.Camera;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import graphics.shadows.ShadowCasterTexture;
import gui.menus.Button;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.menu.*;
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
        GAME_START, GAME_MAIN, GAME_LOBBY, GAME_OPTION, GAME_PAUSE, GAME_INIT, GAME_RUNNING, GAME_OVER, GAME_END,
        GAME_EXIT
    }

    private GLFWErrorCallback errorCallback;
    private static KeyInput keyInput;
    private static MouseButtonInput mouseButtonInput;
    private static MousePosInput mousePosInput;
    private static final int VSYNC = 0;

    private long window;
    private Matrix4f projMatrix;
    private Level level;
    private Camera cam;
    private Player player;
    private AudioSource ambienceSound;
    private ShaderHandler shaderHandler;
    private ResourceHandler resourceHandler;
    private MainMenu mainMenu;
    private LobbyMenu lobbyMenu;
    private OptionsMenu optionsMenu;
    private EndMenu endMenu;
    private PauseMenu pauseMenu;
    public static boolean loop = true;
    public static boolean isGameRunning = false;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static GameStates gameState;
    public static GameStates gameStateOld;

    public GameState(){
        gameState = GameStates.GAME_START;
        shaderHandler = new ShaderHandler();
        resourceHandler = new ResourceHandler();
    }

    public GameStates getGameState(){
        return gameState;
    }

    public void update(){
        switch (gameState){
            case GAME_START:                            // Only run once! Initialize opengl and lwjgl.
                glInit();                               // And menu.
                menuInit();
                gameState = GameStates.GAME_MAIN;
                break;
            case GAME_MAIN:                             // Main menu loop.
                menuCleanUp();
                mainMenu = new MainMenu();
                loop();
                break;
            case GAME_LOBBY:                            // Lobby menu loop.
                menuCleanUp();
                lobbyMenu = new LobbyMenu();
                loop();
                break;
            case GAME_OPTION:                           // Options menu loop.
                menuCleanUp();
                optionsMenu = new OptionsMenu();
                loop();
                break;
            case GAME_PAUSE:
                menuCleanUp();
                loop();
                break;
            case GAME_INIT:                             // Only run once! Initialize a game.
                menuCleanUp();
                gameInit();
                gameState = GameStates.GAME_RUNNING;
                break;
            case GAME_RUNNING:                          // Game running loop.
                menuCleanUp();
                isGameRunning = true;
                loop();
                break;
            case GAME_OVER:                             // Game is over.
                isGameRunning = false;
                gameCleanUp();
                loop();
                break;
            case GAME_END:                              // Exit program.
                exitCleanUp();
                break;
        }
        loop = true;
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
    }

    private void gameInit() {
        cam = new Camera(WIDTH, HEIGHT);
        level = new Level("maps/map_01.tmx");
        projMatrix = cam.getProjection();
        ShadowHandler.calcShadowCaster(level);
        player = new Player();
        AudioMaster.setListenerData(player.getPosition(),new Vector2f(player.getSpeed(), player.getSpeed()));
        ambienceSound = new AudioSource();
        ambienceSound.setPosition(player.getPosition());
        ambienceSound.setLooping(true);
        ambienceSound.setVolume(1);
        ambienceSound.play(ResourceHandler.ambienceSoundBuffer);
        LightHandler.init();
        ShadowHandler.init();
    }

    private void loop() {
        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        System.out.println("Current State: " + gameState);
        System.out.println("Old State    : " + gameStateOld);

        if (isGameRunning) {
            // DO an initial update
            updateLogic();
        }
        while (loop && glfwWindowShouldClose(window) == GLFW_FALSE) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                switch (gameState) {
                    case GAME_MAIN:
                        mainMenu.update();
                        break;
                    case GAME_LOBBY:
                        lobbyMenu.update();
                        break;
                    case GAME_OPTION:
                        if(optionsMenu != null)
                            optionsMenu.update();
                        if(isGameRunning)
                            updateLogic();
                        break;
                    case GAME_RUNNING:
                        updateLogic();
                        break;
                    case GAME_PAUSE:
                        if (pauseMenu == null)
                            pauseMenu = new PauseMenu();
                        pauseMenu.update();
                        updateLogic();
                        break;
                    case GAME_OVER:
                        if (endMenu == null)
                            endMenu = new EndMenu();
                        endMenu.update();
                        break;
                    default:
                        break;
                }
                glfwPollEvents();
                updates++;
                delta--;
            }
            if (isGameRunning)
                renderGame();
            else
                renderMenu();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                glfwSetWindowTitle(window, updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            //TEMP/////////////////////////////////////////////////////////////////////////////////////////
            if (KeyInput.isKeyClicked(GLFW_KEY_O)) {
                gameState = GameStates.GAME_OVER;
                loop = false;
            }
            if (glfwWindowShouldClose(window) == GLFW_TRUE) {
                gameState = GameStates.GAME_END;
                loop = false;
            }
            if (!isGameRunning && KeyInput.isKeyClicked(GLFW_KEY_ESCAPE)) {
                gameState = GameStates.GAME_END;
                loop = false;
            }
            if (isGameRunning && KeyInput.isKeyClicked(GLFW_KEY_ESCAPE)) {
                if (pauseMenu == null) {
                    pauseMenu = new PauseMenu();
                    gameState = GameStates.GAME_PAUSE;
                } else {
                    pauseMenu.destroy();
                    pauseMenu = null;
                    gameState = GameStates.GAME_RUNNING;
                }
            }
            //TEMP/////////////////////////////////////////////////////////////////////////////////////////
        }
    }

    private void updateLogic() {
        cam.update(player.getPosition(), player.getSpeed(), level);
        player.update(level, projMatrix);
        projMatrix = cam.getProjection();
    }

    private void renderGame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        ShadowHandler.calcShadowMap(LightHandler.lightList, projMatrix, level, player);

        level.render(projMatrix, player);
        player.render(projMatrix);
        TextMaster.render();

        glfwSwapBuffers(window);
    }

    private void renderMenu(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        TextMaster.render();

        glfwSwapBuffers(window);
    }

    private void menuCleanUp(){
        if(mainMenu != null)
            mainMenu.destroy();
        if(lobbyMenu != null)
            lobbyMenu.destroy();
        if(endMenu != null)
            endMenu.destroy();
        if(optionsMenu != null)
            optionsMenu.destroy();
        if(pauseMenu != null)
            pauseMenu.destroy();

        mainMenu = null;
        lobbyMenu = null;
        endMenu = null;
        optionsMenu = null;
        pauseMenu = null;
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
        menuCleanUp();
        gameCleanUp();
        AudioMaster.cleanUp();
        if(resourceHandler != null)
            resourceHandler.cleanUp();
        if(shaderHandler != null)
            shaderHandler.cleanUp();
        TextMaster.cleanUp();
        glfwDestroyWindow(window);
        errorCallback.release();
    }
}
