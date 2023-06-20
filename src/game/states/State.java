package game.states;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public abstract class State {

    public static State currentState;

    public abstract void update();
    public abstract void draw(Graphics g);

    public abstract void mouseClicked(MouseEvent e);
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);
    public abstract void mouseEntered(MouseEvent e);
    public abstract void mouseExited(MouseEvent e);
    public abstract void mouseDragged(MouseEvent e);
    public abstract void mouseMoved(MouseEvent e);
}
