package graphics.lighting;


import logic.Player;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class LightHandler {
    public static List<Vector2f> lightList;

    public LightHandler() {}

    public static void init() {
        lightList = new ArrayList<>();
        lightList.add(new Vector2f(18.0f, 14.0f));
    }

    public static void updatePos(Player player) {
        lightList.get(0).x = player.getPosition().x + player.getWidth()/2.0f;
        lightList.get(0).y = player.getPosition().y + player.getHeight()/2.0f;
    }
}
