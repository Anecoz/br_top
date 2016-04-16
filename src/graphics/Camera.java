package graphics;

import org.joml.Interpolationf;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import utils.FileUtils;
import utils.GraphicsUtils;

import java.util.Vector;

public class Camera {
    private Matrix4f projection;
    private Vector2f position;
    private float invAr;
    private final static float WIN_SIZE = 20.0f;
    private static final Matrix4f lookAt = new Matrix4f().lookAt(0f, 0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f);

    private Texture texture;
    private Shader shader;
    private IndexedVertexArray mesh;
    private Vector2f topLeft = new Vector2f(0);
    private Vector2f topRight = new Vector2f(0);
    private Vector2f bottomRight = new Vector2f(0);
    private Vector2f bottomLeft = new Vector2f(0);

    public Camera(int Width, int Height) {
        invAr = (float)Height / (float)Width;
        position = new Vector2f(0);
        projection = new Matrix4f().ortho(0.0f, WIN_SIZE, -WIN_SIZE * invAr, 0.0f, -1.0f, 1.0f);
        texture = new Texture(FileUtils.RES_DIR + "player.png");
        shader = new Shader("player.vert", "player.frag");
        shader.comeHere();
        shader.uploadTexture(0, "tex");
        shader.pissOff();
        mesh = GraphicsUtils.createModelQuad();
    }

    public void setPosition(Vector2f posIn) {
        this.position = posIn;
    }

    public Matrix4f getProjection() {return projection.mul(lookAt);}

    public void update(Vector2f playerPos, float playerSpeed) {
        updateCameraMovement(playerPos, playerSpeed);
        projection = new Matrix4f().ortho(position.x, position.x+WIN_SIZE, -(position.y+WIN_SIZE)*invAr, -position.y*invAr, -1.0f, 1.0f);
    }

    public void render(Matrix4f proj) {
        shader.comeHere();
        texture.bind();
        shader.uploadMatrix(proj, "projMatrix");

        shader.uploadMatrix(new Matrix4f().translate(topLeft.x, topLeft.y, 0f), "modelMatrix");
        mesh.draw();

        shader.uploadMatrix(new Matrix4f().translate(topRight.x, topRight.y, 0f), "modelMatrix");
        mesh.draw();

        shader.uploadMatrix(new Matrix4f().translate(bottomLeft.x, bottomLeft.y, 0f), "modelMatrix");
        mesh.draw();

        shader.uploadMatrix(new Matrix4f().translate(bottomRight.x, bottomRight.y, 0f), "modelMatrix");
        mesh.draw();

        texture.unbind();
        shader.pissOff();
    }

    private void updateCameraMovement(Vector2f playerPos, float playerSpeed) {
        this.topLeft = new Vector2f(position.x + (WIN_SIZE / 3), position.y + (WIN_SIZE * invAr / 3));
        this.topRight = new Vector2f(position.x + ((WIN_SIZE * 2) / 3), position.y + (WIN_SIZE * invAr / 3));
        this.bottomRight = new Vector2f(position.x + ((WIN_SIZE * 2) / 3), position.y + ((WIN_SIZE * invAr * 2) / 3));
        this.bottomLeft = new Vector2f(position.x + (WIN_SIZE / 3), position.y + ((WIN_SIZE * invAr * 2) / 3));

        // Side Check
        if(playerPos.y <= topLeft.y && playerPos.x >= topLeft.x && playerPos.x <= topRight.x) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
        }
        if(playerPos.x >= topRight.x && playerPos.y >= topRight.y && playerPos.y <= bottomRight.y) {
            //System.out.println("Outside Right");
            position.x += playerSpeed;
        }
        if(playerPos.x >= bottomLeft.x && playerPos.x <= bottomRight.x && playerPos.y >= bottomRight.y) {
            //System.out.println("Outside Bottom");
            position.y += playerSpeed;
        }
        if(playerPos.x <= topLeft.x && playerPos.y >= topLeft.y && playerPos.y <= bottomLeft.y) {
            //System.out.println("Outside Left");
            position.x += -playerSpeed;
        }

        // Diagonal Check
        if(playerPos.x <= topLeft.x && playerPos.y <= topLeft.y) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
            position.x += -playerSpeed;
        }
        if(playerPos.x >= topRight.x && playerPos.y <= topRight.y) {
            //System.out.println("Outside Top");
            position.y += -playerSpeed;
            position.x += playerSpeed;
        }
        if(playerPos.x >= bottomRight.x && playerPos.y >= bottomRight.y) {
            //System.out.println("Outside Top");
            position.y += playerSpeed;
            position.x += playerSpeed;
        }
        if(playerPos.x <= bottomLeft.x && playerPos.y >= bottomLeft.y) {
            //System.out.println("Outside Top");
            position.y += playerSpeed;
            position.x += -playerSpeed;
        }
    }
}
