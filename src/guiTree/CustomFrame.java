package guiTree;

import guiTree.events.KeyEventGetter;
import guiTree.events.MouseWheelGetter;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CustomFrame extends JFrame {
    private BufferedImage imageBuffer;
    private Window parentWindow;

    public CustomFrame(Window parent)
    {
        this("", parent);
    }

    public CustomFrame(String name, Window parent)
    {
        super(name);
        this.parentWindow = parent;
        this.addMouseMotionListener(new MouseEventGetter(parent));
        this.addMouseListener(new MouseEventGetter(parent));
        this.addKeyListener(new KeyEventGetter(parent));
        this.addMouseWheelListener(new MouseWheelGetter(parent));
    }

    public void setImageBuffer(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(imageBuffer, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
