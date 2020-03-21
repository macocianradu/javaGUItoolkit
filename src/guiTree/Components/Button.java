package guiTree.Components;

import guiTree.Visual;
import guiTree.events.MouseAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Button extends Visual {
    private String label;
    private Boolean pressed;
    private Boolean hovered;
    private BufferedImage icon;

    public Button() {
        this("", null);
    }

    public Button(String label) {
        this(label, null);
    }

    public Button(BufferedImage icon) {
        this(null, icon);
    }

    public Button(String label, BufferedImage icon) {
        super();
        this.label = label;
        this.icon = icon;
        pressed = false;
        hovered = false;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                pressed = true;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                pressed = false;
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                hovered = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                hovered = false;
                repaint();
            }
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                repaint();
            }
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                repaint();
            }
        });
    }

    @Override
    public void paint(BufferedImage imageBuffer)
    {
        //Get Graphics
        Graphics2D g = imageBuffer.createGraphics();

        //Set Transparency
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setComposite(AlphaComposite.Src);

        //Choose background
        if(hovered || pressed) {
            g.setColor(this.getForegroundColor());
        }
        else {
            g.setColor(this.getBackgroundColor());
        }

        //Draw Button
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(this.getFontColor());

        //Draw Label
        int textWidth = 0;
        int textHeight = 0;
        if(!label.equals("")) {
            textWidth = g.getFontMetrics().stringWidth(label);
            textHeight = g.getFontMetrics().getHeight();
        }

        g.drawString(this.label, (this.getWidth() - textWidth)/2, (this.getHeight() + textHeight)/2);

        //Draw Icon
        if(icon != null) {
            int iconWidth = icon.getWidth();
            int iconHeight = icon.getHeight();

            int iconX = (this.getWidth() - iconWidth - textWidth) / 2;
            int iconY = (this.getHeight() - iconHeight - textHeight) / 2;
            Graphics2D g2 = imageBuffer.createGraphics();
            g2.drawImage(icon, iconX, iconY, null);
            g2.dispose();
        }

        g.dispose();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public void setIcon(String url) {
        try{
            icon = ImageIO.read(new File("resources\\icons\\" + url + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getIcon() {
        return icon;
    }
}
