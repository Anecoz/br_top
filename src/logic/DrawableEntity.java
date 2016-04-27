package logic;

import graphics.Camera;
import graphics.lighting.LightHandler;
import graphics.lowlevel.IndexedVertexArray;
import graphics.lowlevel.Texture;
import graphics.shaders.ShaderHandler;
import graphics.shadows.ShadowHandler;
import logic.inventory.InventoryItem;
import logic.weapons.Ammunition;
import logic.weapons.Bullet;
import logic.weapons.Weapon;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL13;
import utils.Config;
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

    // Some optimization things
    private Matrix4f modelMatrix = new Matrix4f();

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
        if (!(this instanceof Player) && !(this instanceof OtherPlayer)
                && !(this instanceof Weapon)&& !(this instanceof Ammunition))
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

        modelMatrix.identity().translate(position.x, position.y, 0f);
        ShaderHandler.standardShader.uploadMatrix(projection, "projMatrix");
        ShaderHandler.standardShader.uploadMatrix(rotation, "rotationMatrix");
        ShaderHandler.standardShader.uploadMatrix(modelMatrix, "modelMatrix");
        ShaderHandler.standardShader.uploadFloat(Camera.getWinSizeX(), "windowSizeX");
        ShaderHandler.standardShader.uploadFloat(Camera.getWinSizeY(), "windowSizeY");
        ShaderHandler.standardShader.uploadVec(Camera.getPosition(), "camPos");
        ShaderHandler.standardShader.uploadInt(LightHandler.lightList.size(), "numLights");
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
}
