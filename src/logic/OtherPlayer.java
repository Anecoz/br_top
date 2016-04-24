package logic;


import graphics.animation.Animation;
import org.joml.Vector2f;
import utils.ResourceHandler;

// A network player
public class OtherPlayer extends DrawableEntity {
    private Vector2f forward;
    private int id;
    private String displayName;
    private Animation walkingAnimation;

    public OtherPlayer(Vector2f initPos, int id, String displayName) {
        super(ResourceHandler.playerTexture, initPos, -0.3f);

        this.forward = new Vector2f(0);
        this.id = id;
        this.displayName = displayName;
        this.walkingAnimation = ResourceHandler.playerAnimation;

        this.walkingAnimation.start();
        this.mesh = ResourceHandler.playerQuad;
    }

    public int getId() {return id;}

    public void setPosition(Vector2f pos) {
        this.position = pos;
        this.texture = this.walkingAnimation.getFrame();
    }
}
