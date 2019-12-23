package guiTree;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomFrame extends Frame {
    private BufferedImage imageBuffer;

    public CustomFrame()
    {
        super();
    }

    public CustomFrame(String name)
    {
        super(name);
    }

    public void setImageBuffer(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(imageBuffer, 5, 0, this.getWidth(), this.getHeight(), null);
    }
}
