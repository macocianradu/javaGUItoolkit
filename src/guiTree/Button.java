package guiTree;

import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Button extends Visual {
    private String label;
    private Boolean pressed;

    public Button() {
        this("");
    }

    public Button(String label) {
        super();
        this.label = label;
        pressed = false;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                pressed = true;
                revalidate();
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                pressed = false;
                revalidate();
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
        if(pressed) {
            g.setColor(Color.GRAY);
        }
        else {
            g.setColor(this.getBackgroundColor());
        }

        //Draw Button
        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 50, 50);
        g.setColor(this.getForegroundColor());

        //Draw Label
        int textWidth = g.getFontMetrics().stringWidth(label);
        int textHeight = g.getFontMetrics().getHeight();
        g.drawString(this.label, this.getWidth()/2 - textWidth/2, this.getHeight()/2 + textHeight/2);

        g.dispose();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
