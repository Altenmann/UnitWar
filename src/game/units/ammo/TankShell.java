package game.units.ammo;

import java.awt.*;

public class TankShell extends Projectile {

    public TankShell(int x, int y, float angle) {
        super(x, y, angle, 20);
        damage = 20;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(location.x-displaySize/2, location.y-displaySize/2, displaySize, displaySize);
    }
}
