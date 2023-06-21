package game;

import game.states.State;
import game.states.TestState;
import ui.CFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Launcher {

    // Has the canvas and jframe used for displaying the game
    static CFrame cframe;

    // Width and height of the game
    static int width = 500, height = 500;

    static boolean running = false; // Main thread to loop if running
    static int fps = 30; // Frames per second
    static int millisPerUpdate = 1000/fps; // Milliseconds between updates

    static Graphics g;

    public static void main(String[] args) {
        State.currentState = new TestState(width, height);
        cframe = new CFrame(width, height);
        addControls();
        makeThread().start();
    }

    private static void update() {
        if(State.currentState == null) return;

        // Updating the state
        State.currentState.update();


        // Drawing the state

        // Getting the graphics from cframe
        g = cframe.getDrawGraphics();
        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, cframe.getWidth(), cframe.getHeight());

        // Current state drawing
        State.currentState.draw(g);

        // Show what was drawn
        cframe.finalizeDraw(g);
    }

    // Main thread to loop while running is true
    private static Thread makeThread() {
        return new Thread (() -> {
            running = true;
            long time, nextTime;
            while(running) {
                // Set the time constraints for sleeping
                time = System.currentTimeMillis();
                nextTime = time + millisPerUpdate;
                // Call the update function inside of Launcher
                update();
                try { // Sleep the thread till the next time
                    Thread.sleep(nextTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch(IllegalArgumentException e) {
                    System.out.println("Negative sleep time!");
                }
            }
        });
    }

    // Controls for the canvas directed to the current state
    private static void addControls() {
        cframe.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(State.currentState != null) State.currentState.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(State.currentState != null) State.currentState.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(State.currentState != null) State.currentState.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(State.currentState != null) State.currentState.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(State.currentState != null) State.currentState.mouseExited(e);
            }
        });

        cframe.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(State.currentState != null) State.currentState.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(State.currentState != null) State.currentState.mouseMoved(e);
            }
        });
    }
}
