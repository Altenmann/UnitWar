package game.units;

import game.teams.Team;

import java.awt.*;

public class WalkerUnit extends Unit {

    public WalkerUnit(int x, int y, Team team) {
        super(x, y, 1, .5f, team);
        displaySize = 20;
    }

    @Override
    public void update() {
        moveToDesiredLocation();
    }

    @Override
    public void draw(Graphics g) {
        drawDesiredLocation(g);

        g.setColor(Color.red);
        g.fillOval((int)location.x-displaySize/2, (int)location.y-displaySize/2, displaySize, displaySize);
        if(selected) {
            g.setColor(Color.cyan);
            g.drawOval((int)location.x-displaySize/2, (int)location.y-displaySize/2, displaySize, displaySize);
        }
    }

    public void drawDesiredLocation(Graphics g) {
        g.setColor(Color.white);
        g.drawOval((int)desiredLocation.x-displaySize/2, (int)desiredLocation.y-displaySize/2,
                displaySize, displaySize);
    }

    @Override
    public void drawAttackLocation(Graphics g) {

    }

    @Override
    public void attack() {

    }
}
