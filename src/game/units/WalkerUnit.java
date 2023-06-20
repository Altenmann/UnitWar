package game.units;

import java.awt.*;

public class WalkerUnit extends Unit {

    public WalkerUnit(int x, int y) {
        super(x, y, 100, 10, 1, .5f);
        displaySize = 20;
    }

    @Override
    public void update() {
        moveToDesiredLocation();
    }

    @Override
    public void draw(Graphics g) {
        drawRotation(g);
        drawDesiredLocation(g);

        g.setColor(Color.BLACK);
        g.fillOval((int)location.x-displaySize/2, (int)location.y-displaySize/2, displaySize, displaySize);
        if(selected) {
            g.setColor(Color.BLUE);
            g.drawOval((int)location.x-displaySize/2, (int)location.y-displaySize/2, displaySize, displaySize);
        }
    }
}
