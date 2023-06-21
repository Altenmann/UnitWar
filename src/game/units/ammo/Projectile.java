package game.units.ammo;

import game.units.Unit;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class Projectile {

    static ArrayList<Projectile> projectiles = new ArrayList<>();

    static int xMin, xMax, yMin, yMax;

    public static void setBounds(int xMin, int xMax, int yMin, int yMax) {
        Projectile.xMin = xMin;
        Projectile.xMax = xMax;
        Projectile.yMin = yMin;
        Projectile.yMax = yMax;
    }

    protected Point2D.Double location;
    protected Point2D.Double lastLocation;
    protected double angle;

    private double speed;

    protected int displaySize = 5;

    protected int damage;

    protected Unit shooter;

    protected Projectile(int x, int y, float angle, float speed, Unit shooter) {
        location = new Point2D.Double(x, y);
        lastLocation = new Point2D.Double(x, y);
        this.angle = angle;
        this.speed = speed;

        this.shooter = shooter;
    }

    public void update() {
        lastLocation.x = location.x;
        lastLocation.y = location.y;
        location.x += speed*Math.cos(angle);
        location.y += speed*Math.sin(angle);
    }

    public abstract void draw(Graphics g);

    public static void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public static void updateAll() {
        projectiles.removeAll(outOfBoundsProjectiles());
        projectiles.forEach(Projectile::update);
    }

    public static void drawAll(Graphics g) {
        projectiles.forEach(p -> p.draw(g));
    }

    // Gets all projectiles off screen
    private static Collection<Projectile> outOfBoundsProjectiles() {
        return projectiles.stream().filter(p -> (
                p.location.x <= xMin - p.displaySize/2 || p.location.x >= xMax + p.displaySize/2
                || p.location.y <= yMin - p.displaySize/2 || p.location.y > yMax + p.displaySize/2))
                .collect(Collectors.toList());
    }

    public static void checkCollisions(ArrayList<Unit> units) {
        Collection<Projectile> collidedProjectiles = new ArrayList<>();
        projectiles.forEach(p -> {
            units.forEach(unit -> {
                if(p.shooter == unit) return;
                if(unit.inHitBox(p.location, p.lastLocation)) {
                    unit.takeDamage(p.damage);
                    collidedProjectiles.add(p);
                }
            });
        });
        projectiles.removeAll(collidedProjectiles);
    }
}
