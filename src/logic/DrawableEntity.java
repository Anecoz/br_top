package logic;

import graphics.Camera;
import graphics.lighting.LightHandler;
import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import logic.inventory.InventoryItem;
import logic.weapons.Bullet;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL13;
import utils.FileUtils;
import utils.GraphicsUtils;
import utils.ResourceHandler;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;

// Any entity that can be drawn to screen
public abstract class DrawableEntity {

    protected Texture texture;
    protected Vector2f position;
    protected IndexedVertexArray mesh;
    protected Matrix4f rotation;
    protected float width;
    protected float height;
    protected float scale;

    public float getWidth() {return width;}
    public float getHeight() {return height;}
    public Vector2f getPosition() {return position;}
    public Matrix4f getRotation() {return rotation;}

    public DrawableEntity(Texture texture, Vector2f initPos, float layer) {
        this.texture = texture;
        init(layer, initPos);
    }

    private void init(float layer, Vector2f initPos) {
        this.width = this.texture.getWidthAfterScale();
        this.height = this.texture.getHeightAfterScale();
        this.scale = this.texture.getScale();

        this.position = initPos;
        if (!(this instanceof Player) && !(this instanceof OtherPlayer))
            this.mesh = GraphicsUtils.createModelQuad(width, height, layer);
        this.rotation = new Matrix4f();
    }

    public void renderDisplay(Matrix4f projection) {
        doRender(true, projection);
    }

    public void render(Matrix4f projection) {
        doRender(false, projection);
    }

    private void doRender(boolean display, Matrix4f projection) {
        ShaderHandler.standardShader.comeHere();
        GL13.glActiveTexture(GL_TEXTURE0);
        if (display) {
            InventoryItem item = (InventoryItem) this;
            item.getDisplayTexture().bind();
        }
        else {
            texture.bind();
        }
        GL13.glActiveTexture(GL_TEXTURE1);
        ShadowHandler.bindShadowMap();

        ShaderHandler.standardShader.uploadMatrix(projection, "projMatrix");
        ShaderHandler.standardShader.uploadMatrix(rotation, "rotationMatrix");
        ShaderHandler.standardShader.uploadMatrix(new Matrix4f().translate(position.x, position.y, 0f), "modelMatrix");
        ShaderHandler.standardShader.uploadFloat(Camera.getWinSizeX(), "windowSizeX");
        ShaderHandler.standardShader.uploadFloat(Camera.getWinSizeY(), "windowSizeY");
        ShaderHandler.standardShader.uploadVec(Camera.getPosition(), "camPos");
        ShaderHandler.standardShader.uploadInt(1, "numLights");
        ShaderHandler.standardShader.uploadVec(LightHandler.lightList.get(0), "lightPos");
        mesh.draw();

        if (display) {
            InventoryItem item = (InventoryItem) this;
            item.getDisplayTexture().unbind();
        }
        else {
            texture.unbind();
        }
        ShadowHandler.unbindShadowMap();
        ShaderHandler.standardShader.pissOff();
    }

    public void cleanUp() {
        //mesh.cleanUp();
        //texture.cleanUp();
    }
}
