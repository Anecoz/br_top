package logic.weapons;

import graphics.shaders.ShaderHandler;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utils.Config;
import utils.ResourceHandler;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Bullet extends Ammunition {

    private final int DAMAGE = 10;

    public Bullet(Vector2f position, Vector2f velocity, int uniqueId){
        super(ResourceHandler.bulletTexture, ResourceHandler.bulletTexture, position, -0.8f, uniqueId);

        this.damage = DAMAGE;
        this.velocity = velocity;
        this.mesh = ResourceHandler.bulletQuad;
    }

    //----- NOTE: IF THIS IS TO BE USED, REMOVE ALL "new" HERE! CHECK HOW DRAWABLEENTITY DOES IT!
    /*@Override
    public void render(Matrix4f projection){
        ShaderHandler.bulletShader.comeHere();
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        ShaderHandler.bulletShader.uploadMatrix(projection, "projMatrix");
        ShaderHandler.bulletShader.uploadMatrix(rotation, "rotationMatrix");
        ShaderHandler.bulletShader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        ShaderHandler.bulletShader.uploadVec(new Vector2f(Config.CONFIG_RES_WIDTH, Config.CONFIG_RES_HEIGHT), "screenSize");
        ShaderHandler.bulletShader.uploadVec(new Vector4f(0f,0f,0f,0f),"time");
        ShaderHandler.bulletShader.uploadVec(velocity,"velocity");
        ShaderHandler.bulletShader.uploadTexture(0, "texture");

        mesh.draw();

        texture.unbind();
        ShaderHandler.bulletShader.pissOff();
    }*/
}
