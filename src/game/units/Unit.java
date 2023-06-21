package game.units;

import game.teams.Team;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * The Unit class is an abstract class but of which stores
 * the members of all shared aspects between units
 *
 * It also provides static methods for manipulating multiple selected units
 */
public abstract class Unit {

    // Used for storing all the selected units
    public static ArrayList<Unit> selectedUnits = new ArrayList<>();


    // A particular unit's location in the world
    protected Point2D.Double location;
    // Where the unit is ordered to go
    protected Point2D.Double desiredLocation;
    // The unit's desired attack location
    protected Point2D.Double attackLocation;


    // Angles are in radians
    protected double rotation; // Angle unit is facing  (0 is right)
    protected double moveAngle; // Angle on which to move
    protected double finalRotation; // Rotation if not moving


    /**
     * For Weapons
     */
    protected double gunRotation = 0;
    protected double gunRotationSpeed = Math.PI;

    protected double gunCooldown = 1;
    protected long lastFireTime=0;


    protected int health = 100; // Health
    protected int maxHealth = 100;
    protected float speed; // Speed of movement
    protected float rotationSpeed; // Speed of unit rotation

    protected boolean selected = false; // Selection by user

    protected boolean isAlive = true;

    protected boolean isMoving = false;


    protected int displaySize; // The base size of which to display

    protected Team team; // Each unit has a team

    protected Rectangle hitBox;

    protected Unit(int x, int y, double rotation, float speed, float rotationSpeed, Team team) {
        location = new Point2D.Double(x, y);
        desiredLocation = new Point2D.Double(x, y);

        this.rotation = rotation;
        finalRotation = rotation;

        this.speed = speed;
        this.rotationSpeed = (float)(rotationSpeed*Math.PI/8);

        this.team = team;
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    public abstract void showHealth(Graphics g);

    public abstract void drawDesiredLocation(Graphics g);
    public abstract void drawAttackLocation(Graphics g);

    public abstract void attack();

    protected abstract void updateHitBox();

    public void takeDamage(int damageAmount) {
        if(!isAlive) return;
        health -= damageAmount;
        if(health <= 0) {
            die();
        }
    }

    private void die() {
        health = 0;
        isAlive = false;
        selected = false;
        selectedUnits.remove(this);
    }

    public boolean select(boolean selected) {
        if(!isAlive) {
            this.selected = false;
            return false;
        }
        this.selected = selected;
        return true;
    }

    public Point2D.Double getLocation() {
        return location;
    }


    public static void orderAttack(int x, int y) {
        selectedUnits.forEach(unit -> unit.attackLocation = new Point2D.Double(x, y));
    }

    /**
     *
     * TODO: Fix to support infantry and tank spacing and further expansions
     */
    public static void orderMoveTo(int x, int y) {
        Unit su;
        Point2D.Double avgPoint = new Point2D.Double();

        int moveAmount = selectedUnits.size();

        for(int i=0; i<moveAmount; i++) {
            su = selectedUnits.get(i);
            avgPoint.x += su.location.x;
            avgPoint.y += su.location.y;
        }

        avgPoint.x /= moveAmount;
        avgPoint.y /= moveAmount;

        double finalRotation = Math.atan2(y-avgPoint.y, x-avgPoint.x);

        int halfSize = Math.floorDiv(moveAmount, 2);

        for(int i=0; i<moveAmount; i++) {
            su = selectedUnits.get(i);
            double xSpace = i - halfSize + ((moveAmount%2!=0)?0:.5);
            //double ySpace = i - halfSize + ((moveAmount%2==0)?0:.5);
            su.desiredLocation.x = (int) (x + ( su.displaySize*Math.cos(finalRotation + Math.PI/2) * xSpace ));
            su.desiredLocation.y = (int) (y + ( su.displaySize*Math.sin(finalRotation + Math.PI/2) * xSpace ));
            su.setMoveAngle(su.desiredLocation);
            su.finalRotation = finalRotation;
        }
    }

    protected void setMoveAngle(Point2D.Double dp) {
        moveAngle = Math.atan2(dp.y - location.y, dp.x - location.x);
    }

    protected boolean rotateToAngle(double angle) {
        if(rotation >= Math.PI*2) rotation %= Math.PI*2;
        if(rotation < 0) rotation += Math.PI*2;
        if(angle >= Math.PI*2) angle %= Math.PI*2;
        if(angle < 0) angle += Math.PI*2;

        if(rotation == angle) return true;

        float rotationDistance = (float)(angle - rotation);

        float rotateAmount =
                rotationSpeed
                * ((rotationDistance>0)? 1:-1)
                * ((Math.abs(rotationDistance) <= Math.PI)? 1: -1);

        if( Math.abs(rotateAmount) > Math.abs(rotationDistance) ) {
            rotation = angle;
        } else {
            rotation += rotateAmount;
        }

        return false;
    }

    protected void moveToDesiredLocation() {
        if(location.x == desiredLocation.x && location.y == desiredLocation.y) {
            rotateToAngle(finalRotation);
        } else if(rotateToAngle(moveAngle)) {
            double xMove = speed*Math.cos(rotation);
            double yMove = speed*Math.sin(rotation);
            int xSign = (xMove<0)? -1 : 1;
            int ySign = (yMove<0)? -1 : 1;

            if(xSign * (location.x + xMove) > xSign * desiredLocation.x) location.x = desiredLocation.x;
            else location.x += xMove;
            if(ySign * (location.y + yMove) > ySign * desiredLocation.y) location.y = desiredLocation.y;
            else location.y += yMove;
        }
    }

    public boolean inHitBox(Point2D.Double point1, Point2D.Double point2) {
        double dist1 = Point.distance(point1.x, point1.y, location.x, location.y);
        double angle1 = Math.atan2(point1.y-location.y, point1.x-location.x);
        double dist2 = Point.distance(point2.x, point2.y, location.x, location.y);
        double angle2 = Math.atan2(point2.y-location.y, point2.x-location.x);

        if(angle1 < 0) angle1+= Math.PI * 2;
        if(angle2 < 0) angle2 += Math.PI*2;

        double x1 = location.x + Math.cos(angle1) * dist1;
        double y1 = location.y + Math.sin(angle1) * dist1;
        double x2 = location.x + Math.cos(angle2) * dist2;
        double y2 = location.y + Math.sin(angle2) * dist2;

        return hitBox.intersectsLine(x1, y1, x2, y2);
    }

    protected float healthPercent() {
        return Math.min((float)health/maxHealth, 1);
    }
}
