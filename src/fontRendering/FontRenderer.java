package fontRendering;

import java.util.List;
import java.util.Map;

import graphics.shaders.ShaderHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;

public class FontRenderer {

	//private FontShader shader;

	public FontRenderer() {
		//shader = new FontShader();
	}
	
	public void render(Map<FontType, List<GUIText>> texts){
		prepare();
		for(FontType font : texts.keySet()){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text : texts.get(font)){
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanUp(){
		//shader.cleanUp();
	}
	
	private void prepare(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//shader.start();
		ShaderHandler.textShader.comeHere();
	}
	
	private void renderText(GUIText text){
		GL30.glBindVertexArray(text.getMesh());
		//shader.loadColour(text.getColour());
		//shader.loadTranslation(text.getPosition());
		ShaderHandler.textShader.uploadVec(text.getColour(), "colour");
		ShaderHandler.textShader.uploadVec(text.getPosition(), "translation");
		ShaderHandler.textShader.uploadFloat(text.getWidth(), "width");
        ShaderHandler.textShader.uploadFloat(text.getEdge(), "edge");
        ShaderHandler.textShader.uploadFloat(text.getBorderEdge(), "borderEdge");
        ShaderHandler.textShader.uploadFloat(text.getBorderWidth(), "borderWidth");
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount()*3);
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering(){
		//shader.stop();
		ShaderHandler.textShader.pissOff();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
