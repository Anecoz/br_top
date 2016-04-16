package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private Matrix4f projection;
    private Vector2f position;
    private float invAr;
    private final static float WIN_SIZE = 20.0f;
    private static final Matrix4f lookAt = new Matrix4f().lookAt(0f, 0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f);

    public Camera(int Width, int Height) {
        invAr = (float)Height / (float)Width;
        position = new Vector2f(0);
        projection = new Matrix4f().ortho(0.0f, WIN_SIZE, -WIN_SIZE * invAr, 0.0f, -1.0f, 1.0f);
    }

    public void setPosition(Vector2f posIn) {
        this.position = posIn;
    }

    public Matrix4f getProjection() {return projection.mul(lookAt);}

    public void update(Vector2f playerPos, float playerSpeed) {
        updateCameraMovement(playerPos, playerSpeed);
        projection = new Matrix4f().ortho(position.x, position.x+WIN_SIZE, -(position.y+WIN_SIZE)*invAr, -position.y*invAr, -1.0f, 1.0f);
    }

    public void updateCameraMovement(Vector2f playerPos, float playerSpeed) {
        Vector2f topLeft = new Vector2f(position.x + (WIN_SIZE / 3), position.y + (WIN_SIZE * invAr / 3));
        Vector2f topRight = new Vector2f(position.x + ((WIN_SIZE * 2) / 3), position.y + (WIN_SIZE * invAr / 3));
        Vector2f bottomRight = new Vector2f(position.x + ((WIN_SIZE * 2) / 3), position.y + ((WIN_SIZE * invAr * 2) / 3));
        Vector2f bottomLeft = new Vector2f(position.x + (position.x / 3), position.y + ((WIN_SIZE * invAr * 2) / 3));

        if(playerPos.y < topLeft.y && playerPos.x > topLeft.x && playerPos.x < topRight.x) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
        }

        if(playerPos.x > topRight.x && playerPos.y > topRight.y && playerPos.y < bottomRight.y) {
            //System.out.println("Outside Right");
            position.x += playerSpeed;
        }

        if(playerPos.x > bottomLeft.x && playerPos.x < bottomRight.x && playerPos.y > bottomRight.y) {
            //System.out.println("Outside Bottom");
            position.y += playerSpeed;
        }

        if(playerPos.x < topLeft.x && playerPos.y > topLeft.y && playerPos.y < bottomLeft.y) {
            //System.out.println("Outside Left");
            position.x += -playerSpeed;
        }
    }
}
