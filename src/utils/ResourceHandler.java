package utils;

import audio.AudioMaster;
import graphics.animation.Animation;
import graphics.lowlevel.Texture;

// Takes care of providing the Textures and soundsBuffers as statics so that we do not have to
// reinit them.
public class ResourceHandler {

    // Textures
    public static Texture playerTexture;
    public static Texture pistolTexture;
    public static Texture bulletTexture;

    // Sound Buffers
    public static int ambienceSoundBuffer;
    public static int pistolShotSoundBuffer;
    public static int walkingSoundBuffer;

    // Animations
    public static Animation playerAnimation;

    public ResourceHandler(){

    }

    public void init(){
        // Textures
        playerTexture = new Texture(FileUtils.RES_DIR + "characters/player.png");
        pistolTexture = new Texture(FileUtils.RES_DIR + "weapons/pistol.png");
        bulletTexture = new Texture(FileUtils.RES_DIR + "weapons/bullet.png");

        // Sound Buffers
        ambienceSoundBuffer = AudioMaster.loadSound(FileUtils.RES_DIR + "sounds/Ambience_Bird.wav");

        // Animations
        playerAnimation = new Animation(FileUtils.RES_DIR + "characters/playerSpriteSheet.png", 100, 100, 8);
    }

    public void cleanUp(){
        // Textures
        playerTexture.cleanUp();
        pistolTexture.cleanUp();
        bulletTexture.cleanUp();

        // Animations
        playerAnimation.cleanUp();
    }
}
