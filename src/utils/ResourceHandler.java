package utils;

import audio.AudioMaster;
import fontMeshCreator.FontType;
import graphics.animation.Animation;
import graphics.lowlevel.Texture;

import java.io.File;

// Takes care of providing the Textures and soundsBuffers as statics so that we do not have to
// reinit them.
public class ResourceHandler {

    // Textures
    public static Texture playerTexture;
    public static Texture pistolTexture;
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

    public ResourceHandler(){

    }

    public void init(){
        // Textures
        playerTexture = new Texture(FileUtils.RES_DIR + "characters/player.png", 1.0f);
        pistolTexture = new Texture(FileUtils.RES_DIR + "weapons/pistol.png", 1.0f);
        bulletTexture = new Texture(FileUtils.RES_DIR + "weapons/bullet.png", 0.2f);
        fontAtlasTexture = new Texture(FileUtils.RES_DIR + "fonts/gadugi.png");

        // Sound Buffers
        ambienceSoundBuffer = AudioMaster.loadSound(FileUtils.RES_DIR + "sounds/Ambience_Bird.wav");

        // Animations
        playerAnimation = new Animation(FileUtils.RES_DIR + "characters/playerSpriteSheet.png", 100, 100, 8);

        // Fonts
        font = new FontType(ResourceHandler.fontAtlasTexture, new File(FileUtils.RES_DIR + "fonts/gadugi.fnt"));
    }

    public void cleanUp(){
        // Textures
        playerTexture.cleanUp();
        pistolTexture.cleanUp();
        bulletTexture.cleanUp();
        fontAtlasTexture.cleanUp();

        // Animations
        playerAnimation.cleanUp();
    }
}
