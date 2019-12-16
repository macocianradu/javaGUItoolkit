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
        g.setColor(Color.BLUE);
        g.fillRect(10, 33, 500, 500);
    }
}
