package graphics;


import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private Matrix4f projection;
    private Vector2f position;
    private final static float INV_AR = 9.0f/16.0f;
    private final static float WIN_SIZE = 20.0f;
    private static final Matrix4f lookAt = new Matrix4f().lookAt(0f, 0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f);

    public Camera() {
        position = new Vector2f(0);
        projection = new Matrix4f().ortho(0.0f, WIN_SIZE, -WIN_SIZE * INV_AR, 0.0f, -1.0f, 1.0f);
    }

    public void setPosition(Vector2f posIn) {
        this.position = posIn;
    }

    public Matrix4f getProjection() {return projection.mul(lookAt);}

    public void update() {
        projection = new Matrix4f().ortho(position.x, position.x+WIN_SIZE, -(position.y+WIN_SIZE)*INV_AR, -position.y*INV_AR, -1.0f, 1.0f);
    }
}
