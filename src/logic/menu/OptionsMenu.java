package logic.menu;

import graphics.lighting.LightHandler;
import graphics.shadows.ShadowHandler;
import gui.fontMeshCreator.GUIText;
import gui.menus.Button;
import logic.GameState;
import networking.ClientStateHandler;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;
import utils.Config;
import utils.ResourceHandler;

public class OptionsMenu extends OptionsItem {

    private GUIText vsyncValue;
    private GUIText multiSampleValue;
    private GUIText resolutionValue;
    private Vector2f labelPosition = new Vector2f(0.35f, 0.2f);
    private Vector2f valuePosition = new Vector2f(0.6f, 0.2f);
    private float offset = 0.05f;

    public OptionsMenu() {
        //// Texts
        GUIText optionsText = new GUIText("OPTIONS", 3, ResourceHandler.font, new Vector2f(0f, 0.1f), 1f, true);
        optionsText.setColour(1f, 1f, 1f);
        optionsText.setRenderParams(0.5f, 0.1f, 0.6f, 0.1f);

        // VSync
        GUIText vsyncLabel = new GUIText("VSYNC", 1, ResourceHandler.font, labelPosition, 1f, false);
        vsyncValue = new GUIText(Integer.toString(Config.CONFIG_VSYNC),
                1, ResourceHandler.font, valuePosition, 1f, false);
        // Multisample
        GUIText multiSampleLabel = new GUIText("MULTISAMPLE", 1, ResourceHandler.font,
                new Vector2f(labelPosition.x, labelPosition.y + offset), 1f, false);
        multiSampleValue = new GUIText(Integer.toString(Config.CONFIG_SAMPLES),
                1, ResourceHandler.font, new Vector2f(valuePosition.x, valuePosition.y + offset), 1f, false);
        // Resolution
        GUIText resolutionLabel = new GUIText("RESOLUTION", 1, ResourceHandler.font,
                new Vector2f(labelPosition.x, labelPosition.y + offset * 2), 1f, false);
        String resolution = Integer.toString(Config.CONFIG_RES_WIDTH) + "x" + Integer.toString(Config.CONFIG_RES_HEIGHT);
        resolutionValue = new GUIText(resolution,
                1, ResourceHandler.font, new Vector2f(valuePosition.x - offset, valuePosition.y + offset * 2), 1f, false);

        //// Buttons
        // VSync
        Button toggleVsync = new Button("Toggle",  new Vector2f(0.62f, 0.2f)) {
            @Override
            public void callback() {
                swapVsync();
                GameState.loop = false;
            }
        };
        // Multisample
        Button toggleMultiSample = new Button("Toggle",  new Vector2f(0.62f, 0.25f)) {
            @Override
            public void callback() {
                swapMultiSample();
                GameState.loop = false;
            }
        };
        // Resolution
        Button toggleResolution = new Button("Toggle",  new Vector2f(0.62f, 0.3f)) {
            @Override
            public void callback() {
                swapResolution();
                GameState.loop = false;
            }
        };

        Button applyButton = new Button("APPLY",  new Vector2f(0.45f, 0.35f)) {
            @Override
            public void callback() {
                Config.saveConfig();
                if(!GameState.isGameRunning) {
                    GameState.resetCleanup();
                    GameState.createWindow(false);
                    GameState.menuInit();
                }
                GameState.loop = false;
            }
        };
        Button backButton = new Button("BACK",  new Vector2f(0.45f, 0.4f)) {
            @Override
            public void callback() {
                GameState.gameState = GameState.gameStateOld;
                GameState.gameStateOld = GameState.GameStates.GAME_OPTION;
                GameState.loop = false;
            }
        };

        // Add the buttons and texts to MenuItems lists to update them.
        buttonList.add(toggleVsync);
        buttonList.add(toggleMultiSample);
        buttonList.add(backButton);
        buttonList.add(applyButton);
        buttonList.add(toggleResolution);
        textList.add(resolutionLabel);
        textList.add(resolutionValue);
        textList.add(vsyncLabel);
        textList.add(vsyncValue);
        textList.add(multiSampleLabel);
        textList.add(multiSampleValue);
        textList.add(optionsText);
    }
}
