package logic.menu;

import gui.fontMeshCreator.GUIText;
import gui.menus.Button;
import logic.GameState;
import org.joml.Vector2f;
import utils.ResourceHandler;

public class MainMenu extends MenuItem {

    public MainMenu() {
        // Buttons
        Button playButton = new Button("PLAY", new Vector2f(0.45f, 0.3f)) {
            @Override
            public void callback() {
                GameState.gameState = GameState.GameStates.GAME_INIT;
                GameState.loop = false;
            }
        };

        Button exitButton = new Button("EXIT",  new Vector2f(0.45f, 0.4f)) {
            @Override
            public void callback() {
                GameState.gameState = GameState.GameStates.GAME_END;
                GameState.loop = false;
            }
        };

        // Texts
        GUIText mainText = new GUIText("Main Menu", 3, ResourceHandler.font, new Vector2f(0f, 0.1f), 1f, true);
        mainText.setColour(1f, 1f, 1f);
        mainText.setRenderParams(0.5f, 0.1f, 0.6f, 0.1f);

        // Add the buttons and texts to MenuItems lists to update them.
        buttonList.add(playButton);
        buttonList.add(exitButton);
        textList.add(mainText);
    }
}
