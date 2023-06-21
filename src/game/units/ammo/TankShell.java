package game.units.ammo;

import game.units.Unit;

import java.awt.*;

public class TankShell extends Projectile {

    public TankShell(int x, int y, float angle, Unit shooter) {
        super(x, y, angle, 20, shooter);
        damage = 20;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval((int)(location.x-displaySize/2), (int)(location.y-displaySize/2), displaySize, displaySize);
    }
}
