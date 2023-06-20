package game.states;

import game.units.Unit;
import game.units.WalkerUnit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestState extends State {

    private Point selectStart, selectEnd;
    private Rectangle selectRect;
    private boolean dragging = false;

    private Unit walkerUnit;

    private int width, height;

    private BufferedImage mapImg;


    public TestState(BufferedImage mapImg, int width, int height) {
        this.mapImg = mapImg;
        this.width = width;
        this.height = height;



        selectStart = new Point();
        selectEnd = new Point();
        selectRect = new Rectangle();

        walkerUnit = new WalkerUnit(250, 250);
    }

    private void getSelection() {
        walkerUnit.select(selectRect.contains(walkerUnit.getLocation()));
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
        walkerUnit.update();
    }

    /**
     * {@code @method} draw
     * @param g Graphics
     */
    @Override
    public void draw(Graphics g) {
        if(mapImg != null) g.drawImage(mapImg, 0, 0, width, height, null);


        ((Graphics2D) g).setStroke(new BasicStroke(3));

        walkerUnit.draw(g);
        drawSelection(g);
    }

    private void drawSelection(Graphics g) {
        if(selectRect.width == 0 && selectRect.height == 0) return;
        g.setColor(Color.white);
        g.drawRect(selectRect.x, selectRect.y, selectRect.width, selectRect.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("Clicked " + walkerUnit.isSelected());

        if(walkerUnit.isSelected()) {
            walkerUnit.orderMoveTo(e.getX(), e.getY());
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
