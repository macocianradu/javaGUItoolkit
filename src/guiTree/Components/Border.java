package guiTree.Components;

import guiTree.Visual;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Border extends Visual {
    private int thickness;
    private Color color;

    public Border() {
        this(1, Color.BLACK);
    }

    public Border(int thickness) {
        this(thickness, Color.BLACK);
    }

    public Border(Color color) {
        this(1, color);
    }

    public Border(int thickness, Color color) {
        super();
        this.thickness = thickness;
        this.color = color;
        setSize(1.0f, 1.0f);
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
        update();
    }

    public void setColor(Color color) {
        this.color = color;
        update();
    }

    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();

        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawRect(0, 0, getWidth(), getHeight());
    }
}
