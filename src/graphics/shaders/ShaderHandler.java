package graphics.shaders;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

// Takes care of providing the shaders as statics so that we do not have to
// reinit them.
public class ShaderHandler {

    public static Shader levelShader;
    public static Shader standardShader;
    public static Shader bulletShader;
    public static Shader textShader;
    public static Shader inventoryShader;
    public static Shader shadowMapShader;

    private static String WEAPON_DIR = "weapons/";
    private static String SHADOW_DIR = "shadows/";

    public ShaderHandler() {

    }

    public static void init() {
        levelShader = new Shader("level.vert", "level.frag");
        standardShader = new Shader(WEAPON_DIR + "standard.vert", WEAPON_DIR + "standard.frag");
        bulletShader = new Shader(WEAPON_DIR + "bullet.vert", WEAPON_DIR + "bullet.frag");
        textShader = new Shader("font.vert", "font.frag");
        inventoryShader = new Shader("inventory.vert", "inventory.frag");
        shadowMapShader = new Shader(SHADOW_DIR + "shadowMap.vert", SHADOW_DIR + "shadowMap.frag");

        inventoryShader.comeHere();
        inventoryShader.uploadTexture(0, "itemTex");
        inventoryShader.pissOff();

        textShader.comeHere();
        standardShader.uploadTexture(0, "fontAtlas");
        standardShader.pissOff();

        standardShader.comeHere();
        glActiveTexture(GL_TEXTURE0);
        standardShader.uploadTexture(0, "tex");
        glActiveTexture(GL_TEXTURE1);
        standardShader.uploadTexture(1, "shadowTex");
        standardShader.pissOff();

        bulletShader.comeHere();
        glActiveTexture(GL_TEXTURE0);
        bulletShader.uploadTexture(0, "tex");
        glActiveTexture(GL_TEXTURE1);
        bulletShader.uploadTexture(1, "shadowTex");
        bulletShader.pissOff();

        shadowMapShader.comeHere();
        shadowMapShader.uploadTexture(0, "collisionMap");
        shadowMapShader.pissOff();

        levelShader.comeHere();
        glActiveTexture(GL_TEXTURE0);
        levelShader.uploadTexture(0, "atlas");
        glActiveTexture(GL_TEXTURE1);
        levelShader.uploadTexture(1, "shadowTex");
        levelShader.pissOff();
    }

    public static void cleanUp() {
        levelShader.cleanUp();
        standardShader.cleanUp();
        bulletShader.cleanUp();
        textShader.cleanUp();
        inventoryShader.cleanUp();
    }
}
