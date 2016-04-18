package logic.weapons;

import graphics.lowlevel.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Pistol extends Weapon {

    private List<Bullet> bulletList = new ArrayList<>();
    private Texture sprite;
    private Vector2f position;

    public Pistol(Texture sprite, Vector2f position, float layer, float reloadTime, int magazineSize, int roundsPerMinute) {
        super(sprite, position, layer);

        this.reloadTime = reloadTime;
        this.magazineSize = magazineSize;
        this.magazine = magazineSize;
        this.ammo = magazineSize;
        this.roundsPerMinute = roundsPerMinute;
        this.sprite = sprite;
        this.position = position;
    }

    @Override
    public void fire(){
        if (magazine < 1) {
            // TODO: do something l0l
            return;
        }
        super.fire();
        Vector2f bulletPos = new Vector2f(position.x, position.y);
        Vector2f bulletVel = new Vector2f(this.forward.x, this.forward.y);
        bulletList.add(new Bullet(sprite, bulletPos, bulletVel.mul(0.6f), -0.8f, 10));
    }

    @Override
    public void update(Vector2f forward){
        this.forward = forward;
        for(Bullet bullet: bulletList){
            bullet.update();
        }
    }

    @Override
    public void render(Matrix4f projection){
        super.render(projection);
        for(Bullet bullet: bulletList){
            bullet.render(projection);
        }
    }
}
