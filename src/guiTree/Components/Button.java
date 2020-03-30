package guiTree.Components;

import guiTree.Helper.Debugger;
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
    private int round = -1;

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
            public void mouseClicked(MouseEvent mouseEvent) {
                pressed = false;
                update();
            }
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                pressed = true;
                update();
                Debugger.log("Pressed: " + getName(), Debugger.Tag.LISTENER);
                Debugger.log("Calling repaint from pressed: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                pressed = false;
                update();
                Debugger.log("Calling repaint from released: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                hovered = true;
                update();
                Debugger.log("Calling repaint from entered: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                hovered = false;
                update();
                Debugger.log("Calling repaint from exited: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
            }
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Debugger.log("Calling repaint from moved: " + getName(), Debugger.Tag.PAINTING);
            }
        });
    }

    @Override
    public void paint(BufferedImage imageBuffer)
    {
        //Get Graphics
        Graphics2D g = imageBuffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Choose background
        if(hovered) {
            g.setColor(this.getAccentColor());
        }
        else {
            g.setColor(this.getBackgroundColor());
        }
        if(pressed) {
            g.setColor(this.getForegroundColor());
        }

        //Draw Button
        if(getHasBorder()) {
            g.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, round, round);
            g.setColor(getBorderColor());
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, round, round);
        }
        else {
            g.fillRoundRect(0, 0, getWidth(), getHeight(), round, round);
        }

        //Draw Label
        if(getFont() != null) {
            g.setFont(getFont());
        }
        g.setColor(this.getFontColor());
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

    public void setRound(int round) {
        this.round = round;
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
