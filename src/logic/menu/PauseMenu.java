package logic.menu;

import gui.fontMeshCreator.GUIText;
import gui.menus.Button;
import logic.GameState;
import org.joml.Vector2f;
import utils.ResourceHandler;

public class PauseMenu extends MenuItem {

    public PauseMenu() {
        // Buttons
        Button resumeButton = new Button("RESUME",  new Vector2f(0.45f, 0.3f)) {
            @Override
            public void callback() {
                GameState.gameStateOld = GameState.gameState;
                GameState.gameState = GameState.GameStates.GAME_RUNNING;
                GameState.loop = false;
            }
        };
        Button optionButton = new Button("OPTIONS",  new Vector2f(0.45f, 0.35f)) {
            @Override
            public void callback() {
                GameState.gameStateOld = GameState.gameState;
                GameState.gameState = GameState.GameStates.GAME_OPTION;
                GameState.loop = false;
            }
        };
        Button backButton = new Button("EXIT",  new Vector2f(0.45f, 0.4f)) {
            @Override
            public void callback() {
                GameState.gameStateOld = GameState.gameState;
                GameState.gameState = GameState.GameStates.GAME_END;
                GameState.loop = false;
            }
        };

        // Texts
        GUIText optionsText = new GUIText("GAME NOT PAUSED", 3, ResourceHandler.font, new Vector2f(0f, 0.1f), 1f, true);
        optionsText.setColour(1f, 1f, 1f);
        optionsText.setRenderParams(0.5f, 0.1f, 0.6f, 0.1f);

        // Add the buttons and texts to MenuItems lists to update them.
        buttonList.add(resumeButton);
        buttonList.add(backButton);
        buttonList.add(optionButton);
        textList.add(optionsText);
    }
}
