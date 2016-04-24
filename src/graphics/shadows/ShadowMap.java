package graphics.shadows;

import graphics.Camera;
import graphics.lowlevel.Fbo;
import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import graphics.shaders.ShaderHandler;
import logic.GameState;
import logic.Level;
import logic.Player;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import utils.GraphicsUtils;

import java.util.List;

// Gets information about lighting and shadows and creates a texture for it
public class ShadowMap {
    public static int shadowMapID;
    private static Fbo fbo;
    private static IndexedVertexArray quad;

    public ShadowMap() {}

    public static void init() {
        fbo = new Fbo(GameState.WIDTH, GameState.HEIGHT, Fbo.NONE);
        quad = GraphicsUtils.createSimpleQuad();
    }

    public static void calcShadowMap(List<Vector2f> lightArr, Matrix4f proj, Level level, Player player) {
        fbo.bindFrameBuffer();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        ShaderHandler.shadowMapShader.comeHere();

        ShaderHandler.shadowMapShader.uploadMatrix(proj, "projMatrix");
        ShaderHandler.shadowMapShader.uploadMatrix(
                new Matrix4f()
                .translate(Camera.getPosition().x, Camera.getPosition().y, 0.0f)
                .scale(Camera.getWinSizeX(), Camera.getWinSizeY(), 1.0f), "modelMatrix");
        ShaderHandler.shadowMapShader.uploadInt(1, "numLights");
        ShaderHandler.shadowMapShader.uploadVec(lightArr.get(0), "lightPos");
        ShaderHandler.shadowMapShader.uploadVec(new Vector2f(player.getPosition().x + player.getWidth()/2.0f, player.getPosition().y + player.getHeight()/2.0f), "playerPos");
        ShaderHandler.shadowMapShader.uploadInt(level.getBounds().width, "worldWidth");
        ShaderHandler.shadowMapShader.uploadInt(level.getBounds().height, "worldHeight");
        ShaderHandler.shadowMapShader.uploadInt((int)Camera.getWinSizeX(), "windowSize");
        ShadowHandler.shadowCasterTexture.bind();

        quad.draw();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        ShaderHandler.shadowMapShader.pissOff();
        fbo.unbindFrameBuffer();

        shadowMapID = fbo.getColourTexture();
    }
}
