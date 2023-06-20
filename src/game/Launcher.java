package game;

import game.states.State;
import game.states.TestState;
import ui.CFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Launcher {

    static CFrame cframe;

    static int width = 500, height = 500;

    static boolean running = false;
    static int fps = 30;
    static int millisPerUpdate = 1000/fps;

    static Graphics g;

    static BufferedImage usImg;

    public static void main(String[] args) {
        initResources();
        width = usImg.getWidth();
        height = usImg.getHeight();
        State.currentState = new TestState(usImg, width, height);
        cframe = new CFrame(width, height);
        addControls();
        makeThread().start();
    }

    private static void initResources() {
        try {
            usImg = ImageIO.read(Launcher.class.getResource("/united-states-map-with-state-names.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void update() {
        if(State.currentState == null) return;

        State.currentState.update();

        // Getting the graphics from cframe
        g = cframe.getDrawGraphics();
        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, cframe.getWidth(), cframe.getHeight());

        // Current state drawing
        State.currentState.draw(g);

        cframe.finalizeDraw(g);
    }

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
