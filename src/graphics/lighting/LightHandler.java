package graphics.lighting;


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
}
