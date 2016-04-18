package graphics.shadows;


import logic.Level;
import tiled.core.Tile;
import utils.GraphicsUtils;

// Holds information about whether tiles close to player are shadow casting or not
public class ShadowHandler {
    private ShadowTexture shadowTexture;

    public ShadowHandler() {

    }

    public ShadowTexture calcShadowTexture(Level level) {
        shadowTexture = new ShadowTexture();
        int width = (int)level.getBounds().getWidth();
        int height = (int)level.getBounds().getHeight();
        shadowTexture.init(width, height);

        // Loop through tiles close to player and get shadow info
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                Tile currTile = level.getTileAt(x, y);

                if (currTile == null)
                    continue;

                shadowTexture.addAt(x, y, (level.isShadowCastingTile(currTile)) ? 1 : 0);
            }

        // When done, convert to a texture
        shadowTexture.convertToTexture();
        GraphicsUtils.printError("after convert");
        return shadowTexture;
    }
}
