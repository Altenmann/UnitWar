package game.units;

import game.teams.Team;

import java.awt.*;

public class TankUnit extends Unit {

    private double gunRotation = 0;

    public TankUnit(int x, int y, Team team) {
        super(x, y, 3, .3f, team);
        displaySize = 30;
    }

    @Override
    public void update() {
        moveToDesiredLocation();
    }

    @Override
    public void draw(Graphics g) {
        drawDesiredLocation(g);
        drawBody(g, (int) location.x, (int) location.y, rotation, true);
        drawGun(g, (int) location.x, (int) location.y);
    }

    @Override
    public void drawDesiredLocation(Graphics g) {
        drawBody(g, (int)desiredLocation.x, (int)desiredLocation.y, finalRotation, false);
    }

    private void drawBody(Graphics g, int x, int y, double rotation, boolean fill) {

        // Rotate to show the tanks rotation
        ((Graphics2D) g).rotate(rotation, x, y);

        // Fill the body in
        if(fill) {
            g.setColor(teamColor);
            g.fillRect(x - displaySize / 2, y - displaySize / 4, displaySize, displaySize / 2);
        }

        // Show selection
        g.setColor((selected&&fill)?Color.red:team.getSecondaryColor());
        g.drawRect(x-displaySize/2, y-displaySize/4, displaySize, displaySize/2);

        // Rotate the graphics back
        ((Graphics2D) g).rotate(-rotation, x, y);
    }

    private void drawGun(Graphics g, int x, int y) {
        ((Graphics2D) g).rotate(rotation+gunRotation, x, y);

        Rectangle gunRect = new Rectangle(x, y-displaySize/16, displaySize, displaySize/8);
        Rectangle mountRect = new Rectangle(x-displaySize/4, y-displaySize/4, displaySize/2, displaySize/2);

        // Gun fill
        g.setColor(teamColor);
        g.fillRect(gunRect.x, gunRect.y, gunRect.width, gunRect.height);
        // Gun outline
        g.setColor(team.getSecondaryColor());
        g.drawRect(gunRect.x, gunRect.y, gunRect.width, gunRect.height);

        // Gun mount outline
        g.setColor(teamColor);
        g.fillOval(mountRect.x, mountRect.y, mountRect.width, mountRect.height);
        // Gun mount fill
        g.setColor(team.getSecondaryColor());
        g.drawOval(mountRect.x, mountRect.y, mountRect.width, mountRect.height);

        // Rotate the graphics back
        ((Graphics2D) g).rotate(-(rotation+gunRotation), x, y);
    }
}
