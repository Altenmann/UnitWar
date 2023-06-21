package game.units;

import game.teams.Team;
import game.units.ammo.Projectile;
import game.units.ammo.TankShell;

import java.awt.*;

public class TankUnit extends Unit {

    private float gunRotation = 0, gunRotationSpeed = .4f;

    private long lastFireTime=0;
    private float gunCooldown=2;

    public TankUnit(int x, int y, Team team) {
        super(x, y, 3, .3f, team);
        displaySize = 30;
    }

    @Override
    public void update() {
        moveToDesiredLocation();
        attack();
    }

    @Override
    public void draw(Graphics g) {
        drawDesiredLocation(g);
        drawBody(g, (int) location.x, (int) location.y, rotation, true);
        drawGun(g, (int) location.x, (int) location.y);

        drawAttackLocation(g);
    }

    @Override
    public void drawDesiredLocation(Graphics g) {
        drawBody(g, (int)desiredLocation.x, (int)desiredLocation.y, finalRotation, false);
    }

    @Override
    public void drawAttackLocation(Graphics g) {
        if(attackLocation == null) return;
        g.setColor(Color.red);
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g.drawOval((int)attackLocation.x-10, (int)attackLocation.y-10, 20, 20);
        ((Graphics2D) g).setStroke(new BasicStroke(1));
    }

    @Override
    public void attack() {
        if(attackLocation == null) return;

        if(rotateGun(attackLocation.x, attackLocation.y)) {
            fire();
        }
    }

    private void fire() {
        if(System.currentTimeMillis()-lastFireTime < gunCooldown*1000) return;
        lastFireTime = System.currentTimeMillis();
        Projectile.addProjectile(new TankShell((int)location.x, (int)location.y, (float)(rotation+gunRotation)));
    }

    public boolean rotateGun(double x, double y) {
        float targetAngle = (float) Math.atan2(y-location.y, x-location.x);

        float totalRotation = (float) ( rotation + gunRotation );



        if(totalRotation >= Math.PI*2) totalRotation %= Math.PI*2;
        if(totalRotation < 0) totalRotation += Math.PI*2;
        if(targetAngle >= Math.PI*2) targetAngle %= Math.PI*2;
        if(targetAngle < 0) targetAngle += Math.PI*2;

        if(totalRotation == targetAngle) return true;


        float rotationDistance = targetAngle - totalRotation;

        float rotateAmount =
                gunRotationSpeed
                        * ((rotationDistance>0)? 1:-1)
                        * ((Math.abs(rotationDistance) <= Math.PI)? 1: -1);

        if( Math.abs(rotateAmount) > Math.abs(rotationDistance) ) {
            gunRotation = (float) (targetAngle - rotation);
            return true;
        } else {
            gunRotation += rotateAmount;
        }


        return false;
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
