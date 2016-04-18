package graphics.shaders;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

// Takes care of providing the shaders as statics so that we do not have to
// reinit them
public class ShaderHandler {

    public static Shader levelShader;
    public static Shader standardShader;

    private static String WEAPON_DIR = "weapons/";

    public ShaderHandler() {

    }

    public void init() {
        levelShader = new Shader("level.vert", "level.frag");
        standardShader = new Shader(WEAPON_DIR + "standard.vert", WEAPON_DIR + "standard.frag");

        standardShader.comeHere();
        standardShader.uploadTexture(0, "tex");
        standardShader.pissOff();

        levelShader.comeHere();
        glActiveTexture(GL_TEXTURE0);
        levelShader.uploadTexture(0, "atlas");
        glActiveTexture(GL_TEXTURE1);
        levelShader.uploadTexture(1, "shadowTex");
        levelShader.pissOff();
    }
}
