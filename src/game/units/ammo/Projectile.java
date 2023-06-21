package game.units.ammo;

import game.states.TestState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Projectile {

    static ArrayList<Projectile> projectiles = new ArrayList<>();

    static int xMin, xMax, yMin, yMax;

    public static void setBounds(int xMin, int xMax, int yMin, int yMax) {
        Projectile.xMin = xMin;
        Projectile.xMax = xMax;
        Projectile.yMin = yMin;
        Projectile.yMax = yMax;
    }

    protected Point location;
    protected float angle;

    private float speed;

    protected int displaySize = 5;

    protected Projectile(int x, int y, float angle, float speed) {
        location = new Point(x, y);
        this.angle = angle;
        this.speed = speed;
    }

    public void update() {
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
}
