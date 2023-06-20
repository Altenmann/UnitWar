package game.units;

import game.teams.Team;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class Unit {

    public static ArrayList<Unit> selectedUnits = new ArrayList<>();

    protected Point2D.Double location;
    protected Point2D.Double desiredLocation;


    //protected ArrayList<Point> path = new ArrayList<>();

    // Used to determine which way they are facing
    protected double rotation = 0;
    protected double moveAngle;
    protected double finalRotation;

    protected int health, damage;
    protected float speed, rotationSpeed;

    protected boolean selected = false;

    // Strictly for displaying purposes
    protected int displaySize;

    protected Team team;
    protected Color teamColor;

    protected Unit(int x, int y, float speed, float rotationSpeed, Team team) {
        location = new Point2D.Double(x, y);
        desiredLocation = new Point2D.Double(x, y);

        this.speed = speed;
        this.rotationSpeed = (float)(rotationSpeed*Math.PI/8);

        this.team = team;
        teamColor = team.getTeamColor();

        damage = 0;
        health = 1;
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    public void drawRotation(Graphics g) {
        g.setColor(Color.yellow);
        g.drawLine((int)location.x, (int)location.y,
                (int)(location.x+30*Math.cos(rotation)), (int)(location.y+30*Math.sin(rotation)));
    }

    public abstract void drawDesiredLocation(Graphics g);

    public void select(boolean selected) {
        this.selected = selected;
    }

    public Point2D.Double getLocation() {
        return location;
    }

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
            su.desiredLocation.x = x + ( su.displaySize*Math.cos(finalRotation + Math.PI/2) * xSpace );//* (i-selectedUnits.size()/2) );
            su.desiredLocation.y = y + ( su.displaySize*Math.sin(finalRotation + Math.PI/2) * xSpace );//* (i-selectedUnits.size()/2) );
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

    public boolean isSelected() {
        return selected;
    }
}
