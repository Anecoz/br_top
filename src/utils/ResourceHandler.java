package utils;

import audio.AudioMaster;
import graphics.lowlevel.IndexedVertexArray;
import gui.fontMeshCreator.FontType;
import graphics.animation.Animation;
import graphics.lowlevel.Texture;

import java.io.File;

// Takes care of providing the Textures and soundsBuffers as statics so that we do not have to
// reinit them.
public class ResourceHandler {

    // Textures
    public static Texture playerTexture;
    public static Texture pistolTexture;
    public static Texture assaultRifleTexture;
    public static Texture assaultRifleTextureDisplay;
    public static Texture bulletTexture;
    public static Texture fontAtlasTexture;

    // Sound Buffers
    public static int ambienceSoundBuffer;
    public static int pistolShotSoundBuffer;
    public static int walkingSoundBuffer;

    // Animations
    public static Animation playerAnimation;

    // Fonts
    public static FontType font;

    // Meshes
    public static IndexedVertexArray playerQuad;
    public static IndexedVertexArray assaultRifleQuad;
    public static IndexedVertexArray pistolQuad;
    public static IndexedVertexArray bulletQuad;

    public ResourceHandler(){

    }

    public static void init(){
        // Textures
        playerTexture = new Texture(FileUtils.RES_DIR + "characters/player.png", 1.0f);
        pistolTexture = new Texture(FileUtils.RES_DIR + "weapons/pistol.png", 1.0f);
        assaultRifleTexture = new Texture(FileUtils.RES_DIR + "weapons/assault_rifle.png", 1.0f);
        assaultRifleTextureDisplay = new Texture(FileUtils.RES_DIR + "weapons/assault_rifle_display.png", 1.0f);
        bulletTexture = new Texture(FileUtils.RES_DIR + "weapons/bullet.png", 0.2f);
        fontAtlasTexture = new Texture(FileUtils.RES_DIR + "fonts/candara.png");

        // Sound Buffers
        ambienceSoundBuffer = AudioMaster.loadSound(FileUtils.RES_DIR + "sounds/Ambience_Bird.wav");

        // Animations
        playerAnimation = new Animation(FileUtils.RES_DIR + "characters/playerSpriteSheet.png", 100, 100, 8);

        // Fonts
        font = new FontType(ResourceHandler.fontAtlasTexture, new File(FileUtils.RES_DIR + "fonts/candara.fnt"));

        // Meshes
        playerQuad = GraphicsUtils.createModelQuad(playerTexture.getWidthAfterScale(), playerTexture.getHeightAfterScale(), -0.3f);
        assaultRifleQuad = GraphicsUtils.createModelQuad(assaultRifleTexture.getWidthAfterScale(), assaultRifleTexture.getHeightAfterScale(), -0.2f);
        pistolQuad = GraphicsUtils.createModelQuad(pistolTexture.getWidthAfterScale(), pistolTexture.getHeightAfterScale(), -0.2f);
        bulletQuad = GraphicsUtils.createModelQuad(bulletTexture.getWidthAfterScale(), bulletTexture.getHeightAfterScale(), -0.2f);
    }

    public static void cleanUp(){
        // Textures
        playerTexture.cleanUp();
        pistolTexture.cleanUp();
        assaultRifleTexture.cleanUp();
        assaultRifleTextureDisplay.cleanUp();
        bulletTexture.cleanUp();
        fontAtlasTexture.cleanUp();

        // Animations
        playerAnimation.cleanUp();

        // Meshes
        playerQuad.cleanUp();
        pistolQuad.cleanUp();
        assaultRifleQuad.cleanUp();
    }
}
