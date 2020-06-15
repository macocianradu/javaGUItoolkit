package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Helper.Debugger;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ToggleButton extends Visual {
    private String label;
    private Boolean pressed;
    private BufferedImage icon;

    public ToggleButton() {
        this("", null);
    }

    public ToggleButton(String label) {
        this(label, null);
    }

    public ToggleButton(BufferedImage icon) {
        this(null, icon);
    }

    public ToggleButton(String label, BufferedImage icon) {
        super();
        this.label = label;
        this.icon = icon;
        pressed = false;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(pressed) {
                    addAnimation(new ColorAnimation(ToggleButton.this, getForegroundColor(), getAccentColor(), 100));
                }
                else {
                    addAnimation(new ColorAnimation(ToggleButton.this, getAccentColor(), getForegroundColor(), 100));
                }
                pressed = !pressed;

                Debugger.log("Pressed: " + getName(), Debugger.Tag.LISTENER);
                Debugger.log("Calling repaint from pressed: " + getName(), Debugger.Tag.PAINTING);
                update();
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if(!pressed) {
                    addAnimation(new ColorAnimation(ToggleButton.this, getBackgroundColor(), getAccentColor(), 100));
                }
                Debugger.log("Calling repaint from entered: " + getName(), Debugger.Tag.PAINTING);
                update();
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(!pressed) {
                    addAnimation(new ColorAnimation(ToggleButton.this, getAccentColor(), getBackgroundColor(), 100));
                }
                Debugger.log("Calling repaint from exited: " + getName(), Debugger.Tag.PAINTING);
                update();
            }
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Debugger.log("Calling repaint from moved: " + getName(), Debugger.Tag.PAINTING);
            }
        });
    }

    @Override
    public void paint(Image imageBuffer)
    {
        //Get Graphics
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
        g.setColor(getPaintColor());

        //Draw Button
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

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
            Graphics2D g2 = (Graphics2D)imageBuffer.getGraphics();
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
            InputStream iconStream = getClass().getClassLoader().getResourceAsStream("icons/" + url + ".png");
            assert iconStream != null;
            icon = ImageIO.read(iconStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void setPressed(Boolean pressed) {
        this.pressed = pressed;
    }

    public Boolean getPressed() {
        return pressed;
    }
}
