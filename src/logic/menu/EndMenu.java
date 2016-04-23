package logic.menu;

import gui.fontMeshCreator.GUIText;
import gui.menus.Button;
import logic.GameState;
import org.joml.Vector2f;
import utils.ResourceHandler;

public class EndMenu extends MenuItem {

    public EndMenu() {
        // Buttons
        Button mainButton = new Button("MAIN MENU",  new Vector2f(0.45f, 0.4f)) {
            @Override
            public void callback() {
                GameState.gameState = GameState.GameStates.GAME_MAIN;
                GameState.loop = false;
            }
        };

        // Texts
        GUIText overText = new GUIText("GAME OVER", 3, ResourceHandler.font, new Vector2f(0f, 0.1f), 1f, true);
        overText.setColour(1f, 1f, 1f);
        overText.setRenderParams(0.5f, 0.1f, 0.6f, 0.1f);

        // Add the buttons and texts to MenuItems lists to update them.
        buttonList.add(mainButton);
        textList.add(overText);
    }
}
