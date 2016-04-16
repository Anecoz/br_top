package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathUtils {

    public static Vector2f screenSpaceToWorld(Vector2f screenPoint, int screenWidth, int screenHeight, Matrix4f viewProj) {
        double x = 2.0 * (double)screenPoint.x / (double) screenWidth - 1.0;
        double y = -2.0 * (double)screenPoint.y / (double) screenHeight + 1.0;
        Matrix4f invProj = viewProj.invert();

        Vector3f out = new Vector3f((float)x, (float)y, 0.0f);
        invProj.transformPosition(out);
        return new Vector2f(out.x, out.y);
    }
}
