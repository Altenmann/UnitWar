package game.states;

import game.teams.Team;
import game.units.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TestState extends State {

    private Point selectStart, selectEnd;
    private Rectangle selectRect;
    private boolean dragging = false;

    private Unit walkerUnit, tankUnit;

    private ArrayList<Unit> units = new ArrayList<>();


    private int width, height;


    public TestState(int width, int height) {
        this.width = width;
        this.height = height;

        // The selecting rectangle points
        selectStart = new Point();
        selectEnd = new Point();
        selectRect = new Rectangle();

        Team team1 = new Team(Color.blue, Color.cyan);
        Team team2 = new Team(Color.green, Color.darkGray);

        // Units to add
        tankUnit = new TankUnit(350, 400, team1);
        units.add(tankUnit);

        for(int i=0; i<3; i++) {
            units.add(new TankUnit(50, i*50+50, team2));
        }
    }

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
        units.forEach(Unit::update);
    }

    /**
     * {@code @method} draw
     * @param g Graphics
     */
    @Override
    public void draw(Graphics g) {
        //((Graphics2D) g).setStroke(new BasicStroke(2));

        units.forEach(unit -> unit.draw(g));
        drawSelection(g);
    }

    private void drawSelection(Graphics g) {
        if(selectRect.width == 0 && selectRect.height == 0) return;
        g.setColor(Color.white);
        g.drawRect(selectRect.x, selectRect.y, selectRect.width, selectRect.height);
    }

    private void getSelection() {
        Unit.selectedUnits.clear();
        units.forEach( unit -> {
            if( selectRect.contains(unit.getLocation()) ) {
                unit.select(true);
                Unit.selectedUnits.add(unit);
            } else {
                unit.select(false);
                //Unit.selectedUnits.remove(unit);
            }
        } );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("Clicked " + walkerUnit.isSelected());
        Unit.orderMoveTo(e.getX(), e.getY());
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
        //System.out.println("Released " + walkerUnit.isSelected());
        if(dragging) {
            convertSelectedPoints();
            getSelection();
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
