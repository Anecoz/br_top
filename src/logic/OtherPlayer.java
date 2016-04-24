package logic;


import graphics.animation.Animation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
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
        this.texture = walkingAnimation.getFrame();
        this.mesh = ResourceHandler.playerQuad;
    }

    public int getId() {return id;}

    public void setPosition(Vector2f pos) {
        this.position = pos;
        //this.texture = this.walkingAnimation.getFrame();
    }

    public void setForward(Vector2f forward) {
        this.forward = forward;
        // Update rotation matrix
        double centerX = this.position.x + this.width/2.0f;
        double centerY = this.position.y + this.height/2.0f;
        Vector3f center = new Vector3f((float) centerX, (float) centerY, -0.3f);
        Vector2f up = new Vector2f(0.0f, -1.0f);
        rotation = new Matrix4f()
                .translate(center)
                .rotate(forward.angle(up), 0.0f, 0.0f, -1.0f)
                .translate(center.negate());
    }
}
