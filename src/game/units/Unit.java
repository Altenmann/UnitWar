package game.units;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class Unit {

    protected Point2D.Double location;
    protected Point2D.Double desiredLocation;


    //protected ArrayList<Point> path = new ArrayList<>();

    // Used to determine which way they are facing
    protected double rotation = 0;
    protected double desiredRotation;

    protected int health, damage;
    protected float speed, rotationSpeed;

    protected boolean selected = false;

    // Strictly for displaying purposes
    protected int displaySize;

    protected Unit(int x, int y, int health, int damage, float speed, float rotationSpeed) {
        location = new Point2D.Double(x, y);
        desiredLocation = new Point2D.Double(x, y);
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.rotationSpeed = (float)(rotationSpeed*Math.PI/8);
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    public void drawRotation(Graphics g) {
        g.setColor(Color.darkGray);
        g.drawLine((int)location.x, (int)location.y,
                (int)(location.x+30*Math.cos(rotation)), (int)(location.y+30*Math.sin(rotation)));
    }

    public void drawDesiredLocation(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval((int)desiredLocation.x-displaySize/2, (int)desiredLocation.y-displaySize/2,
                displaySize, displaySize);
    }

    public void select(boolean selected) {
        this.selected = selected;
    }

    public Point2D.Double getLocation() {
        return location;
    }

    public void orderMoveTo(int x, int y) {
        desiredLocation.x = x;
        desiredLocation.y = y;
        desiredRotation = Math.atan2(y-location.y, x-location.x);
    }

    protected boolean rotateToDesiredAngle() {
        if(rotation == desiredRotation) return true;
        if(rotation > Math.PI*2) rotation %= Math.PI*2;
        if(rotation < 0) rotation += Math.PI*2;
        if(desiredRotation < 0) desiredRotation += Math.PI*2;

        float rotationDistance = (float)(desiredRotation - rotation);

        float rotateAmount =
                rotationSpeed
                * ((rotationDistance>0)? 1:-1)
                * ((Math.abs(rotationDistance) <= Math.PI)? 1: -1);

        if( Math.abs(rotateAmount) > Math.abs(rotationDistance) ) {
            rotation = desiredRotation;
        } else {
            rotation += rotateAmount;
        }

        return false;
    }

    protected void moveToDesiredLocation() {
        if(rotateToDesiredAngle()) {
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

    public boolean isSelected() {
        return selected;
    }
}
