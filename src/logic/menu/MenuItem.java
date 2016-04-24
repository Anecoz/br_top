package logic.menu;

import gui.fontMeshCreator.GUIText;
import gui.menus.Button;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuItem {

    protected List<Button> buttonList = new ArrayList<>();
    protected List<GUIText> textList = new ArrayList<>();

    public MenuItem() {

    }

    public void update() {
        for(Button button : buttonList){
            button.update();
        }
    }

    public void destroy() {
        for(Button button : buttonList){
            button.remove();
        }
        for(GUIText text : textList){
            text.remove();
        }
        buttonList.clear();
        textList.clear();
    }
}
