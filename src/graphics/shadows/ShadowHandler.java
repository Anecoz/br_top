package graphics.shadows;

import static org.lwjgl.opengl.GL11.*;
import logic.Level;
import logic.Player;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import tiled.core.Tile;
import utils.GraphicsUtils;

import java.util.List;

// Holds information about whether tiles close to player are shadow casting or not
public class ShadowHandler {
    public static ShadowCasterTexture shadowCasterTexture;

    public ShadowHandler() {

    }

    public static void init() {
        ShadowMap.init();
    }

    public static void calcShadowMap(List<Vector2f> lightArr, Matrix4f proj, Level level, Player player) {
        ShadowMap.calcShadowMap(lightArr, proj, level, player);
    }

    public static void bindShadowMap() {
        glBindTexture(GL_TEXTURE_2D, ShadowMap.shadowMapID);
    }

    public static void unbindShadowMap() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void calcShadowCaster(Level level) {
        shadowCasterTexture = new ShadowCasterTexture();
        int width = (int)level.getBounds().getWidth();
        int height = (int)level.getBounds().getHeight();
        shadowCasterTexture.init(width, height);

        // Loop through tiles close to player and get collision info
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                Tile currTile = level.getTileAt(x, y);

                if (currTile == null)
                    continue;

                shadowCasterTexture.addAt(x, y, (level.isShadowCastingTile(currTile)) ? 1 : 0);
            }

        // When done, convert to a texture
        shadowCasterTexture.convertToTexture();
        GraphicsUtils.printError("after convert");
        //return shadowCasterTexture;
    }


}
