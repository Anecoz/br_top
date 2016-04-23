package logic.menu;

import gui.fontMeshCreator.GUIText;
import gui.menus.Button;
import logic.GameState;
import org.joml.Vector2f;
import utils.ResourceHandler;

public class OptionsMenu extends MenuItem {

    public OptionsMenu() {
        // Buttons
        Button backButton = new Button("BACK",  new Vector2f(0.45f, 0.4f)) {
            @Override
            public void callback() {
                GameState.gameState = GameState.GameStates.GAME_END;
                GameState.loop = false;
            }
        };

        // Texts
        GUIText optionsText = new GUIText("OPTIONS", 3, ResourceHandler.font, new Vector2f(0f, 0.1f), 1f, true);
        optionsText.setColour(1f, 1f, 1f);
        optionsText.setRenderParams(0.5f, 0.1f, 0.6f, 0.1f);

        // Add the buttons and texts to MenuItems lists to update them.
        buttonList.add(backButton);
        textList.add(optionsText);
    }
}
