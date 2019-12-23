package guiTree;

import guiTree.Visual;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button extends Visual {

    public Button()
    {
        super();
    }

    @Override
    public void paint(BufferedImage imageBuffer)
    {
        Graphics g = imageBuffer.getGraphics();
        g.setColor(this.getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
