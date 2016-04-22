package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import logic.Level;

// FRIENDLY REMINDER:
// Z: LAYER -1.1f IS CLOSEST TO CAMERA
// Z: LAYER < 0.9 IS FURTHEST AWAY
public class Camera {
    private Matrix4f projection;
    private static Vector2f position;
    private float invAr;
    private static float WIN_SIZE_X = 20.0f;
    private static float WIN_SIZE_Y;
    private static final Matrix4f lookAt = new Matrix4f().lookAt(0f, 0f, -0.1f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f);

    public Camera(int Width, int Height) {
        invAr = (float) Height / (float) Width;
        WIN_SIZE_Y = invAr * WIN_SIZE_X;
        position = new Vector2f(0);
        projection = new Matrix4f().ortho(0.0f, WIN_SIZE_X, -WIN_SIZE_Y, 0.0f, -1.0f, 1.0f);
    }

    public static float getWinSizeY() {return WIN_SIZE_Y;}
    public static float getWinSizeX() {return WIN_SIZE_X;}
    public static void setPosition(Vector2f posIn) {
        position = posIn;
    }
    public static Vector2f getPosition() {return position;}

    public Matrix4f getProjection() {
        return projection.mul(lookAt);
    }

    public void update(Vector2f playerPos, float playerSpeed, Level level) {
        updateCameraMovement(playerPos, playerSpeed, level);
        projection = new Matrix4f().ortho(position.x, position.x + WIN_SIZE_X, -(position.y + WIN_SIZE_Y), -position.y, -1.0f, 1.0f);
    }

    private void updateCameraMovement(Vector2f playerPos, float playerSpeed, Level level) {
        Vector2f topLeft = new Vector2f(position.x + (WIN_SIZE_X / 3), position.y + (WIN_SIZE_Y / 3));
        Vector2f topRight = new Vector2f(position.x + ((WIN_SIZE_X * 2) / 3), position.y + (WIN_SIZE_Y / 3));
        Vector2f bottomRight = new Vector2f(position.x + ((WIN_SIZE_X * 2) / 3), position.y + ((WIN_SIZE_Y * 2) / 3));
        Vector2f bottomLeft = new Vector2f(position.x + (WIN_SIZE_X / 3), position.y + ((WIN_SIZE_Y * 2) / 3));

        Vector2f tempPos = new Vector2f(position.x, position.y);

        // Side Check
        if (playerPos.y <= topLeft.y && playerPos.x >= topLeft.x && playerPos.x <= topRight.x) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
        }
        if (playerPos.x >= topRight.x && playerPos.y >= topRight.y && playerPos.y <= bottomRight.y) {
            //System.out.println("Outside Right");
            position.x += playerSpeed;
        }
        if (playerPos.x >= bottomLeft.x && playerPos.x <= bottomRight.x && playerPos.y >= bottomRight.y) {
            //System.out.println("Outside Bottom");
            position.y += playerSpeed;
        }
        if (playerPos.x <= topLeft.x && playerPos.y >= topLeft.y && playerPos.y <= bottomLeft.y) {
            //System.out.println("Outside Left");
            position.x += -playerSpeed;
        }

        // Diagonal Check
        if (playerPos.x <= topLeft.x && playerPos.y <= topLeft.y) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
            position.x += -playerSpeed;
        }
        if (playerPos.x >= topRight.x && playerPos.y <= topRight.y) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
            position.x += playerSpeed;
        }
        if (playerPos.x >= bottomRight.x && playerPos.y >= bottomRight.y) {
            //System.out.println("Outside Top");
            position.y += playerSpeed;
            position.x += playerSpeed;
        }
        if (playerPos.x <= bottomLeft.x && playerPos.y >= bottomLeft.y) {
            //System.out.println("Outside Top");
            position.y += playerSpeed;
            position.x += -playerSpeed;
        }

        // Check camera map bounds
        if(position.x < level.getBounds().x) {
            //System.out.println("Outside Left");
            position.x = tempPos.x;
        }
        if(position.y < level.getBounds().y) {
            //System.out.println("Outside Top");
            position.y = tempPos.y;
        }
        if((position.x + WIN_SIZE_X) > level.getBounds().width) {
            //System.out.println("Outside Right");
            position.x = tempPos.x;
        }
        if((position.y + WIN_SIZE_Y) > level.getBounds().height) {
            //System.out.println("Outside Bottom");
            position.y = tempPos.y;
        }
    }
}
