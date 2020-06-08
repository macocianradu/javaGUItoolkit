package guiTree.Components.Decorations;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Border extends Decoration {
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

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();

        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawRect(thickness/2, thickness/2, getWidth() - thickness, getHeight() - thickness);
        g.dispose();
    }
}
