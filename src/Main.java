import audio.AudioMaster;
import audio.AudioSource;
import graphics.Camera;
import graphics.shadows.ShadowHandler;
import graphics.shadows.ShadowTexture;
import input.KeyInput;
import input.MouseButtonInput;
import input.MousePosInput;
import logic.Level;
import logic.Player;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.*;
import org.joml.*;
import utils.FileUtils;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

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
    private ShadowHandler shadowHandler;
    private ShadowTexture shadowTexture;
    private AudioSource ambienceSound;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public void run() {
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
        cam = new Camera(WIDTH, HEIGHT);
        level = new Level("maps/map_01.tmx");
        player = new Player("characters/player.png");
        projMatrix = cam.getProjection();
        shadowHandler = new ShadowHandler();
        shadowTexture = shadowHandler.calcShadowTexture(level);
        AudioMaster.init();
        AudioMaster.setListenerData(player.getPosition(),new Vector2f(player.getSpeed(), player.getSpeed()));
        int ambienceSoundBuffer = AudioMaster.loadSound(FileUtils.RES_DIR + "sounds/Ambience_Bird.wav");
        ambienceSound = new AudioSource();
        ambienceSound.setPosition(player.getPosition());
        ambienceSound.setLooping(false);
        ambienceSound.setVolume(1);
        ambienceSound.play(ambienceSoundBuffer);

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
        player.update(cam, level, projMatrix);
        cam.update(player.getPosition(), player.getSpeed(), level);
        projMatrix = cam.getProjection();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Texture shadowMap = shadowFrameBuffer.calcShadowMap(shadowTexture, level, player.getPosition(), projMatrix);
        level.render(projMatrix, shadowTexture, player);
        player.render(projMatrix);

        glfwSwapBuffers(window);
    }

    private void cleanUp(){
        ambienceSound.delete();
        AudioMaster.cleanUp();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}