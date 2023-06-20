package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

// A JFrame made up of a Canvas
public class CFrame extends JFrame {

    private Canvas canvas;
    private BufferStrategy bs;

    public CFrame(int width, int height) {
        super(); // Call the JFrame constructor
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close program on jframe close

        // Make the canvas and size it 1 bigger
        canvas = new Canvas();
        canvas.setSize(width+1,height+1);
        // Add canvas, pack the jframe, set visible
        add(canvas);
        pack();
        setVisible(true);
        // Set the buffer strategy for the canvas
        canvas.createBufferStrategy(3);
    }

    // Get the current buffer's graphics
    public Graphics getDrawGraphics() {
        bs = canvas.getBufferStrategy();
        return bs.getDrawGraphics();
    }

    // Used for showing the buffer and disposing the graphics
    public void finalizeDraw(Graphics g) {
        g.dispose();
        bs.show();
    }

    // Returns the canvas width (hides JFrame getWidth())
    public int getWidth() {
        return canvas.getWidth();
    }

    // Returns the canvas height (hides JFrame getHeight())
    public int getHeight() {
        return canvas.getHeight();
    }

    // Controls for the canvas
    // Mouse actions
    public void addMouseListener(MouseListener ml) {
        canvas.addMouseListener(ml);
    }
    // Mouse movements
    public void addMouseMotionListener(MouseMotionListener mml) {
        canvas.addMouseMotionListener(mml);
    }

}
