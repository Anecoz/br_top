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

    // Some optimization vectors
    private static Vector2f renderPos = new Vector2f(0);
    private static Matrix4f modelMatrix = new Matrix4f();

    public ShadowMap() {}

    public static void init() {
        fbo = new Fbo(GameState.WIDTH, GameState.HEIGHT, Fbo.NONE);
        quad = GraphicsUtils.createSimpleQuad();
    }

    public static void calcShadowMap(List<Vector2f> lightArr, Matrix4f proj, Level level, Player player) {
        fbo.bindFrameBuffer();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        ShaderHandler.shadowMapShader.comeHere();

        renderPos.x = player.getPosition().x + player.getWidth()/2.0f;
        renderPos.y = player.getPosition().y + player.getHeight()/2.0f;
        modelMatrix.identity().translate(Camera.getPosition().x, Camera.getPosition().y, 0.0f).scale(Camera.getWinSizeX(), Camera.getWinSizeY(), 1.0f);
        ShaderHandler.shadowMapShader.uploadMatrix(proj, "projMatrix");
        ShaderHandler.shadowMapShader.uploadMatrix(modelMatrix, "modelMatrix");
        ShaderHandler.shadowMapShader.uploadInt(1, "numLights");
        ShaderHandler.shadowMapShader.uploadVec(lightArr.get(0), "lightPos");
        ShaderHandler.shadowMapShader.uploadVec(renderPos, "playerPos");
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
