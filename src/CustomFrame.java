import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomFrame extends Frame {
    private BufferedImage imageBuffer;

    public CustomFrame()
    {
        super();
        this.addWindowStateListener(e -> {
            imageBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            imageBuffer.getGraphics().setColor(Color.WHITE);
            imageBuffer.getGraphics().fillRect(0, 0, getWidth(), getHeight());
            imageBuffer.getGraphics().setColor(Color.BLACK);
        });
    }

    public BufferedImage getImageBuffer() {
        return this.imageBuffer;
    }

    public CustomFrame(String name)
    {
        super(name);
    }

    @Override
    public void setSize(Dimension d)
    {
        this.setSize(d.width, d.height);
    }

    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        this.imageBuffer.getGraphics().setColor(Color.WHITE);
        this.imageBuffer.getGraphics().fillRect(0, 0, this.getWidth(), this.getHeight());
        this.imageBuffer.getGraphics().setColor(Color.BLACK);
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(imageBuffer, 5, 0, this.getWidth(), this.getHeight(), null);
    }
}
