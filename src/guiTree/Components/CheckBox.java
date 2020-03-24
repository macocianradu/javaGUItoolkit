package guiTree.Components;

import guiTree.Helper.Debugger;
import guiTree.Helper.Tag;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CheckBox extends Visual {
    private BufferedImage icon;
    private boolean hovered;
    private boolean marked;
    private String text;

    public CheckBox() {
        this(false, "");
    }

    public CheckBox(Boolean marked) {
        this(marked, "");
    }

    public CheckBox(String text) {
        this(false, text);
    }

    public CheckBox(Boolean value, String text) {
        super();
        this.marked = value;
        this.text = text;
        setAccentColor(new Color(0.6f, 0.6f, 0.6f, 0.5f));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                marked = !marked;
                Debugger.log("Calling repaint from pressed: " + getName(), Tag.PAINTING);
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                hovered = true;
                Debugger.log("Calling repaint from entered: " + getName(), Tag.PAINTING);
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                hovered = false;
                Debugger.log("Calling repaint from exited: " + getName(), Tag.PAINTING);
                repaint();
            }
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Debugger.log("Calling repaint from moved: " + getName(), Tag.PAINTING);
                repaint();
            }
        });
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();

        //Set Transparency
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setComposite(AlphaComposite.Src);

        if(hovered && !marked) {
            g.setColor(getAccentColor());
            g.fillRect(0, 0, getHeight() - 1, getHeight() - 1);
        }

        if(marked) {
            g.setColor(getForegroundColor());
            g.fillRect(1, 1, getHeight() - 2, getHeight() - 2);

            if(icon != null) {
                int iconWidth = icon.getWidth();
                int iconHeight = icon.getHeight();

                int iconX = (this.getHeight() - iconWidth) / 2;
                int iconY = (this.getHeight() - iconHeight) / 2;
                Graphics2D g2 = imageBuffer.createGraphics();
                g2.drawImage(icon, iconX, iconY, null);
                g2.dispose();
            }
        }

        g.setColor(getFontColor());
        if(getFont() != null) {
            g.setFont(getFont());
        }
        int textHeight = g.getFontMetrics().getHeight();
        g.drawString(text, getHeight() + 10, getHeight() / 2 + textHeight / 4);

        g.setColor(getBorderColor());
        g.drawRect(0, 0, getHeight() - 1, getHeight() - 1);
        g.dispose();
    }
}
