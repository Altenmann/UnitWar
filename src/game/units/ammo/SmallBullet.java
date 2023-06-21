package game.units.ammo;

import game.units.Unit;

import java.awt.*;

public class SmallBullet extends Projectile {
    public SmallBullet(int x, int y, float v, Unit shooter) {
        super(x, y, v, 25, shooter);
        damage = 2;
        displaySize = 2;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.drawOval((int)(location.x-displaySize/2), (int)(location.y-displaySize/2), displaySize, displaySize);
    }
}
