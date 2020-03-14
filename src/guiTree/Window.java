package guiTree;

import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;

public class Window extends Visual {
    public CustomFrame frame;

    public Window()
    {
        super();
        this.frame = new CustomFrame(this);
        this.addWindowStateListener(e -> {
            this.setSize(getWidth(), getHeight());
            revalidate();
        });
    }

    @Override
    public void setSize(Integer width, Integer height)
    {
        this.frame.setSize(width, height);
        super.setSize(width, height);
    }

    public void setFrameImageBuffer(BufferedImage imageBuffer){
        this.frame.setImageBuffer(imageBuffer);
        this.frame.repaint();
    }

    public void repaint() {
        this.frame.repaint();
    }

    public void setSize(Dimension dimension) {
        this.setSize(dimension.width, dimension.height);
    }

    public void setVisible(Boolean visible)
    {
        frame.setVisible(visible);
    }

    public void addWindowListener(WindowListener listener)
    {
        frame.addWindowListener(listener);
    }

    public void addWindowStateListener(WindowStateListener listener)
    {
        frame.addWindowStateListener(listener);
    }

    public void dispose()
    {
        frame.dispose();
    }

    public void repaint(long tm) {
        frame.repaint(tm);
    }

    public void setPositionRelativeTo(Component c){
        frame.setLocationRelativeTo(c);
    }

    public void setPositionRelativeTo(){
        frame.setLocationRelativeTo(null);
    }

    public void setUndecorated(Boolean undecorated){frame.setUndecorated(undecorated);}
}
