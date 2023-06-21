package game.units;

import game.teams.Team;
import game.units.ammo.Projectile;
import game.units.ammo.SmallBullet;

import java.awt.*;

public class InfantryUnit extends Unit {

    public InfantryUnit(int x, int y, double rotation, Team team) {
        super(x, y, rotation, 1, 1, team);
        displaySize = 10;

        hitBox = new Rectangle(x-displaySize/2, y-displaySize/2, displaySize, displaySize);
    }

    @Override
    public void update() {
        if(!isAlive) return;
        moveToDesiredLocation();
        updateHitBox();

        attack();
    }

    @Override
    public void draw(Graphics g) {
        if(isAlive) drawDesiredLocation(g);
        drawBody(g, hitBox.x, hitBox.y, true);

        if(isAlive) drawAttackLocation(g);
    }

    private void drawBody(Graphics g, int x, int y, boolean fill) {
        ((Graphics2D)g).rotate(rotation, x+hitBox.width/2, y+hitBox.height/2);

        if(fill) {
            g.setColor((isAlive) ? team.getTeamColor() : Color.gray);
            g.fillOval(x, y, hitBox.width, hitBox.height);
        } else {
            g.setColor(team.getSecondaryColor());
            g.drawOval(x, y, hitBox.width, hitBox.height);
        }

        if(selected&&fill) {
            g.setColor(Color.red);
            g.drawOval(x, y, hitBox.width, hitBox.height);
        }

        if(fill) {
            showHealth(g);
        }


        ((Graphics2D)g).rotate(-rotation, x+hitBox.width/2, y+hitBox.height/2);
    }

    @Override
    public void showHealth(Graphics g) {
        Rectangle healthRect = new Rectangle((int)location.x-displaySize/2, (int)location.y-displaySize/2,
                displaySize/3, displaySize);
        g.setColor(Color.white);
        g.fillRect(healthRect.x, healthRect.y, healthRect.width, healthRect.height);
        g.setColor(Color.red);
        g.fillRect(healthRect.x, healthRect.y, healthRect.width, (int)(healthPercent() * healthRect.height));
        g.setColor(team.getSecondaryColor());
        g.drawRect(healthRect.x, healthRect.y, healthRect.width, healthRect.height);
    }

    @Override
    public void drawDesiredLocation(Graphics g) {
        drawBody(g, (int) desiredLocation.x - hitBox.width/2, (int) desiredLocation.y - hitBox.width/2, false);
    }

    @Override
    public void drawAttackLocation(Graphics g) {
        if(attackLocation == null) return;
        g.setColor(Color.red);
        g.drawOval((int)attackLocation.x-5, (int)attackLocation.y-5, 10, 10);
    }

    @Override
    public void attack() {
        if(attackLocation == null) return;

        double desiredAngle = Math.atan2(attackLocation.y-location.y, attackLocation.x-location.x);
        if(desiredAngle < 0) desiredAngle += Math.PI*2;

        finalRotation = desiredAngle;

        if(rotation == desiredAngle) {
            fire();
        }

    }

    protected void fire() {
        if(System.currentTimeMillis()-lastFireTime < gunCooldown*1000) return;
        lastFireTime = System.currentTimeMillis();
        Projectile.addProjectile(new SmallBullet((int)location.x, (int)location.y, (float)(rotation+gunRotation), this));
    }

    @Override
    protected void updateHitBox() {
        hitBox.x = (int) (location.x - displaySize/2);
        hitBox.y = (int) (location.y - displaySize/2);
    }
}
