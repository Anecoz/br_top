package logic.weapons;

import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL13;
import utils.Config;
import utils.ResourceHandler;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Bullet extends Ammunition {

    public Bullet(Vector2f position, Vector2f velocity, float layer, int damage, int uniqueId){
        super(ResourceHandler.bulletTexture, ResourceHandler.bulletTexture, position, layer, uniqueId);

        this.damage = damage;
        this.velocity = velocity;
    }

    @Override
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
    }
}
