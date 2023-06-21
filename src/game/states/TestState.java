package game.states;

import game.teams.Team;
import game.units.*;
import game.units.ammo.Projectile;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TestState extends State {

    // Point and Rectangle used for selecting units
    private final Point selectStart = new Point();
    private final Point selectEnd = new Point();
    private final Rectangle selectRect = new Rectangle();

    // Whether the user is dragging or not
    private boolean dragging = false;

    // An all-purpose unit array list to perform actions on all of them
    private final ArrayList<Unit> units = new ArrayList<>();


    private final int width;
    private final int height;


    public TestState(int width, int height) {
        this.width = width;
        this.height = height;

        // Initialize the projectile bounds
        Projectile.setBounds(0, width, 0, height);

        // Two different teams and their colors
        Team team1 = new Team(Color.blue, Color.cyan);
        Team team2 = new Team(Color.green, Color.darkGray);

        // Adding units for team 2
        for(int i=0; i<3; i++) {
            units.add(new TankUnit(50, i*50+50, 0, team1));
            units.add(new TankUnit(width-50, height-(i*50+50), Math.PI, team2));

            units.add(new InfantryUnit(200, i*50+50, 0, team1));
            units.add(new InfantryUnit(200, height-(i*50+50), Math.PI, team2));
        }
    }

    // Gets the selected start and end points and turns them into a rectangle
    private void convertSelectedPoints() {
        int x = selectStart.x;
        int y = selectStart.y;
        int width = selectEnd.x-selectStart.x;
        int height = selectEnd.y-selectStart.y;
        if(width < 0) {
            width *= -1;
            x -= width;
        }
        if(height < 0) {
            height *= -1;
            y -= height;
        }
        selectRect.x = x;
        selectRect.y = y;
        selectRect.width = width;
        selectRect.height = height;
    }

    @Override
    public void update() {
        // Update each unit
        units.forEach(Unit::update);
        // Update every projectile
        Projectile.updateAll();
        Projectile.checkCollisions(units);
    }


    @Override
    public void draw(Graphics g) {
        // Draw all projectiles
        Projectile.drawAll(g);
        // Call the draw method for each unit
        units.forEach(unit -> unit.draw(g));
        // Draw the selection rectangle
        drawSelection(g);
    }

    // Draws the rectangle of the user's selection
    private void drawSelection(Graphics g) {
        if(selectRect.width == 0 && selectRect.height == 0) return;
        g.setColor(Color.white);
        g.drawRect(selectRect.x, selectRect.y, selectRect.width, selectRect.height);
    }


    // Selects and adds the unit to the Unit array list to be controlled by user
    private void getUnitsInSelection() {
        Unit.selectedUnits.clear();
        units.forEach( unit -> {
            if( selectRect.contains(unit.getLocation()) ) {
                if(unit.select(true)) Unit.selectedUnits.add(unit);
            } else {
                unit.select(false);
            }
        } );
    }



    /*
     * Control Methods for user input
     * TODO: Add keyboard events and controls for them
     */

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            Unit.orderAttack(e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            Unit.orderMoveTo(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectStart.x = e.getX();
        selectStart.y = e.getY();
        selectEnd.x = selectStart.x;
        selectEnd.y = selectStart.y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(dragging) {
            convertSelectedPoints();
            getUnitsInSelection();
            selectRect.width = 0;
            selectRect.height = 0;
        }
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        selectEnd.x = e.getX();
        selectEnd.y = e.getY();
        if(!(Math.abs(selectEnd.y-selectStart.y) < 2 && Math.abs(selectEnd.x-selectStart.x) < 2)) {
            convertSelectedPoints();
            dragging = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
