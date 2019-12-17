import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;

public class Window extends Visual {
    public CustomFrame frame;

    public Window()
    {
        super();
        this.frame = new CustomFrame();
        this.addWindowStateListener(e -> {
            this.setSize(getWidth(), getHeight());
            revalidate();
        });
    }

    @Override
    public void setSize(int width, int height)
    {
        this.frame.setSize(width, height);
        super.setSize(width, height);
        revalidate();
    }

    public void setFrameImageBuffer(BufferedImage imageBuffer){
        this.frame.setImageBuffer(imageBuffer);
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

    public void setPositionRelativeTo(Component c){
        frame.setLocationRelativeTo(c);
    }
}
