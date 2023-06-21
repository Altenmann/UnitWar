package game.units;

import game.teams.Team;
import game.units.ammo.Projectile;
import game.units.ammo.TankShell;

import java.awt.*;

public class TankUnit extends Unit {

    public TankUnit(int x, int y, double rotation, Team team) {
        super(x, y, rotation, 2, .1f, team);
        displaySize = 30;

        hitBox = new Rectangle(x-displaySize/2, y-displaySize/4, displaySize, displaySize/2);

        gunRotationSpeed *= .01;
    }

    @Override
    public void update() {
        if(!isAlive) return;
        moveToDesiredLocation();
        updateHitBox();

        attack();
    }

    @Override
    protected void updateHitBox() {
        hitBox.x = (int) location.x - hitBox.width/2;
        hitBox.y = (int) location.y - hitBox.height/2;
    }

    @Override
    public void draw(Graphics g) {
        if(isAlive) drawDesiredLocation(g);
        drawBody(g, hitBox.x, hitBox.y, rotation, true);
        drawGun(g, (int) location.x, (int) location.y);

        if(isAlive) drawAttackLocation(g);
    }

    private void drawBody(Graphics g, int x, int y, double rotation, boolean fill) {

        // Rotate to show the tanks rotation
        ((Graphics2D) g).rotate(rotation, x+hitBox.width/2, y+hitBox.height/2);

        // Fill the body in
        if(fill) {
            g.setColor((isAlive)?team.getTeamColor():Color.gray);
            g.fillRect(x, y, hitBox.width, hitBox.height);
        }

        // Show selection
        g.setColor((selected&&fill)?Color.red:team.getSecondaryColor());
        g.drawRect(x, y, hitBox.width, hitBox.height);

        // Health bar
        if(fill) {
            showHealth(g);
        }

        // Rotate the graphics back
        ((Graphics2D) g).rotate(-rotation, x+hitBox.width/2, y+hitBox.height/2);
    }

    private void drawGun(Graphics g, int x, int y) {
        ((Graphics2D) g).rotate(rotation+gunRotation, x, y);

        Rectangle gunRect = new Rectangle(x, y-displaySize/16, displaySize, displaySize/8);
        Rectangle mountRect = new Rectangle(x-displaySize/4, y-displaySize/4, displaySize/2, displaySize/2);

        // Gun fill
        g.setColor((isAlive)?team.getTeamColor():Color.gray);
        g.fillRect(gunRect.x, gunRect.y, gunRect.width, gunRect.height);
        // Gun outline
        g.setColor(team.getSecondaryColor());
        g.drawRect(gunRect.x, gunRect.y, gunRect.width, gunRect.height);

        // Gun mount outline
        g.setColor((isAlive)?team.getTeamColor():Color.gray);
        g.fillOval(mountRect.x, mountRect.y, mountRect.width, mountRect.height);
        // Gun mount fill
        g.setColor(team.getSecondaryColor());
        g.drawOval(mountRect.x, mountRect.y, mountRect.width, mountRect.height);

        // Rotate the graphics back
        ((Graphics2D) g).rotate(-(rotation+gunRotation), x, y);
    }

    @Override
    public void showHealth(Graphics g) {
        Rectangle healthRect = new Rectangle((int)location.x-displaySize/2, (int)location.y-displaySize/4,
                displaySize/6, displaySize/2);
        g.setColor(Color.white);
        g.fillRect(healthRect.x, healthRect.y, healthRect.width, healthRect.height);
        g.setColor(Color.red);
        g.fillRect(healthRect.x, healthRect.y, healthRect.width, (int)(healthPercent() * healthRect.height));
        g.setColor(team.getSecondaryColor());
        g.drawRect(healthRect.x, healthRect.y, healthRect.width, healthRect.height);
    }

    @Override
    public void drawDesiredLocation(Graphics g) {
        drawBody(g, (int)desiredLocation.x-hitBox.width/2, (int)desiredLocation.y- hitBox.height/2, finalRotation, false);
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
        Projectile.addProjectile(new TankShell((int)location.x, (int)location.y, (float)(rotation+gunRotation), this));
    }

    public boolean rotateGun(double x, double y) {
        double targetAngle = Math.atan2(y-location.y, x-location.x);

        double totalRotation = ( rotation + gunRotation );

        // Clamp angle values to 0 and 2*PI
        if(gunRotation >= Math.PI*2) gunRotation %= Math.PI*2;
        else if(gunRotation < 0) gunRotation += Math.PI*2;
        if(totalRotation >= Math.PI*2) totalRotation %= Math.PI*2;
        else if(totalRotation < 0) totalRotation += Math.PI*2;
        if(targetAngle >= Math.PI*2) targetAngle %= Math.PI*2;
        else if(targetAngle < 0) targetAngle += Math.PI*2;

        if(totalRotation == targetAngle) return true;


        double rotationDistance = targetAngle - totalRotation;

        double rotateAmount =
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
}
