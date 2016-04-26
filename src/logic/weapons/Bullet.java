package logic.weapons;

import graphics.Camera;
import graphics.lighting.LightHandler;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import logic.inventory.InventoryItem;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL13;
import utils.ResourceHandler;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;

public class Bullet extends Ammunition {

    public Bullet(Vector2f position, Vector2f velocity, float layer, int damage, int uniqueId){
        super(ResourceHandler.bulletTexture, ResourceHandler.bulletTexture, position, layer, uniqueId);

        this.damage = damage;
        this.velocity = velocity;
    }

    /*@Override
    public void render(Matrix4f projection){
        ShaderHandler.bulletShader.comeHere();
        GL13.glActiveTexture(GL_TEXTURE0);
        texture.bind();
        GL13.glActiveTexture(GL_TEXTURE1);
        ShadowHandler.bindShadowMap();

        ShaderHandler.bulletShader.uploadMatrix(projection, "projMatrix");
        ShaderHandler.bulletShader.uploadMatrix(rotation, "rotationMatrix");
        ShaderHandler.bulletShader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        ShaderHandler.bulletShader.uploadFloat(Camera.getWinSizeX(), "windowSizeX");
        ShaderHandler.bulletShader.uploadFloat(Camera.getWinSizeY(), "windowSizeY");
        ShaderHandler.bulletShader.uploadVec(Camera.getPosition(), "camPos");
        ShaderHandler.bulletShader.uploadInt(LightHandler.lightList.size(), "numLights");
        ShaderHandler.bulletShader.uploadVec(LightHandler.lightList.get(0), "lightPos");

        mesh.draw();

        texture.unbind();
        ShadowHandler.unbindShadowMap();
        ShaderHandler.bulletShader.pissOff();
    }*/
}
