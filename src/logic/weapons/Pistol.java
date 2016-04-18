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
        super.fire();
        bulletList.add(new Bullet(sprite, position, new Vector2f(1), 10));
    }

    @Override
    public void update(){

    }

    @Override
    public void render(Matrix4f projection){
        super.render(projection);
        for(Bullet bullet: bulletList){
            bullet.render(projection);
        }
    }
}
