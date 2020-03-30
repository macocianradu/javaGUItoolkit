package guiTree.Components;

import guiTree.Helper.Debugger;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScrollPanel extends Visual {

    private float positionX;
    private float positionY;
    private float ratioX;
    private float ratioY;
    private Slider verticalScrollBar;
    private Slider horizontalScrollBar;

    public ScrollPanel() {
        super();
        setName("ScrollPanel");
        horizontalScrollBar = new Slider(Slider.Direction.Horizontal);
        verticalScrollBar = new Slider(Slider.Direction.Vertical);
        verticalScrollBar.setHasBorder(true);
        horizontalScrollBar.setHasBorder(true);
        addVisual(verticalScrollBar);
        addVisual(horizontalScrollBar);
        verticalScrollBar.setName("vertical scroll bar");
        horizontalScrollBar.setName("horizontal scroll bar");
    }

    @Override
    public void setSize() {
        super.setSize();
        if(verticalScrollBar != null && horizontalScrollBar != null) {
            verticalScrollBar.setSize(20, getHeight() - 20);
            horizontalScrollBar.setSize(getWidth() - 20, 20);
            return;
        }

        if(horizontalScrollBar != null) {
            horizontalScrollBar.setSize(getWidth(), 20);
        }

        if(verticalScrollBar != null) {
            verticalScrollBar.setSize(20, getHeight());
        }
    }

    @Override
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();
        if(getHasBorder()) {
            g.setColor(getBackgroundColor());
            g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
            g.setColor(getBorderColor());
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        else {
            g.setColor(getBackgroundColor());
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        g.dispose();
    }
}
