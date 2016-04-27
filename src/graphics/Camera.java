package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import logic.Level;

// FRIENDLY REMINDER:
// Z: LAYER -1.1f IS CLOSEST TO CAMERA
// Z: LAYER < 0.9 IS FURTHEST AWAY
public class Camera {
    private Matrix4f projection = new Matrix4f();
    private static Vector2f position;
    private float invAr;
    private static float WIN_SIZE_X = 20.0f;
    private static float WIN_SIZE_Y;
    private static final Matrix4f lookAt = new Matrix4f().lookAt(0f, 0f, -0.1f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f);

    // A few optimizing vectors
    private Vector2f topLeft = new Vector2f(0);
    private Vector2f topRight = new Vector2f(0);
    private Vector2f bottomRight = new Vector2f(0);
    private Vector2f bottomLeft = new Vector2f(0);
    private Vector2f tempPos = new Vector2f(0);

    public Camera(int Width, int Height) {
        invAr = (float) Height / (float) Width;
        WIN_SIZE_Y = invAr * WIN_SIZE_X;
        position = new Vector2f(0);
    }

    public static float getWinSizeY() {return WIN_SIZE_Y;}
    public static float getWinSizeX() {return WIN_SIZE_X;}
    public static void setPosition(Vector2f posIn) {
        position = posIn;
    }
    public static Vector2f getPosition() {return position;}

    public Matrix4f getProjection() {
        return projection.identity().ortho(position.x, position.x + WIN_SIZE_X, -(position.y + WIN_SIZE_Y), -position.y, -1.0f, 1.0f).mul(lookAt);//.mul(lookAt);
    }

    public void update(Vector2f playerPos, float playerSpeed, Level level) {
        updateCameraMovement(playerPos, playerSpeed, level);
    }

    private void updateCameraMovement(Vector2f playerPos, float playerSpeed, Level level) {
        // Looks ugly, but prevents us from reallocating a vector each frame (which we definitely want to avoid)
        topLeft.x = position.x + (WIN_SIZE_X / 3);
        topLeft.y = position.y + (WIN_SIZE_Y / 3);
        topRight.x = position.x + ((WIN_SIZE_X * 2) / 3);
        topRight.y = position.y + (WIN_SIZE_Y / 3);
        bottomRight.x = position.x + ((WIN_SIZE_X * 2) / 3);
        bottomRight.y = position.y + ((WIN_SIZE_Y * 2) / 3);
        bottomLeft.x = position.x + (WIN_SIZE_X / 3);
        bottomLeft.y = position.y + ((WIN_SIZE_Y * 2) / 3);
        tempPos.x = position.x;
        tempPos.y = position.y;

        // Side Check
        if (playerPos.y <= topLeft.y && playerPos.x >= topLeft.x && playerPos.x <= topRight.x) {
            position.y += -playerSpeed;
        }
        if (playerPos.x >= topRight.x && playerPos.y >= topRight.y && playerPos.y <= bottomRight.y) {
            position.x += playerSpeed;
        }
        if (playerPos.x >= bottomLeft.x && playerPos.x <= bottomRight.x && playerPos.y >= bottomRight.y) {
            position.y += playerSpeed;
        }
        if (playerPos.x <= topLeft.x && playerPos.y >= topLeft.y && playerPos.y <= bottomLeft.y) {
            position.x += -playerSpeed;
        }

        // Diagonal Check
        if (playerPos.x <= topLeft.x && playerPos.y <= topLeft.y) {
            position.y += -playerSpeed;
            position.x += -playerSpeed;
        }
        if (playerPos.x >= topRight.x && playerPos.y <= topRight.y) {
            position.y += -playerSpeed;
            position.x += playerSpeed;
        }
        if (playerPos.x >= bottomRight.x && playerPos.y >= bottomRight.y) {
            position.y += playerSpeed;
            position.x += playerSpeed;
        }
        if (playerPos.x <= bottomLeft.x && playerPos.y >= bottomLeft.y) {
            position.y += playerSpeed;
            position.x += -playerSpeed;
        }

        // Check camera map bounds
        if(position.x < level.getBounds().x) {
            position.x = tempPos.x;
        }
        if(position.y < level.getBounds().y) {
            position.y = tempPos.y;
        }
        if((position.x + WIN_SIZE_X) > level.getBounds().width) {
            position.x = tempPos.x;
        }
        if((position.y + WIN_SIZE_Y) > level.getBounds().height) {
            position.y = tempPos.y;
        }
    }
}
