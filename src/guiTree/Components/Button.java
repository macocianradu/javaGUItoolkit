package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Button extends MenuItem {
    private String label;
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
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                update();
            }
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                addAnimation(new ColorAnimation(Button.this, getAccentColor(), getForegroundColor(), 100));
                update();
                Debugger.log("Pressed: " + getName(), Debugger.Tag.LISTENER);
                Debugger.log("Calling repaint from pressed: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                addAnimation(new ColorAnimation(Button.this, getForegroundColor(), getAccentColor(), 100));
                update();
                Debugger.log("Calling repaint from released: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                addAnimation(new ColorAnimation(Button.this, getBackgroundColor(), getAccentColor(), 100));
                update();
                Debugger.log("Calling repaint from entered: " + getName(), Debugger.Tag.PAINTING);
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                addAnimation(new ColorAnimation(Button.this, getAccentColor(), getBackgroundColor(), 100));
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
    public void setPaintColor(Color color) {
        super.setPaintColor(color);
    }

    @Override
    public void paint(Image imageBuffer) {
        //Get Graphics
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(getPaintColor());

        //Draw Button
        g.fillRoundRect(0, 0, getWidth(), getHeight(), round, round);

        //Get Sizes
        int textWidth = 0;
        int textHeight = 0;
        int iconWidth = 0;
        int iconHeight = 0;
        if(icon != null) {
            iconWidth = Math.min(icon.getWidth(), getWidth());
            iconHeight = Math.min(icon.getHeight(), getHeight());
        }

        if(!label.equals("")) {
            textWidth = g.getFontMetrics().stringWidth(label);
            textHeight = g.getFontMetrics().getHeight();
        }

        //Draw Icon
        if(icon != null) {
            int iconX;
            if(textWidth != 0) {
                iconX = (getWidth() - iconWidth - textWidth - 10) / 2;
            }
            else {
                iconX = (getWidth() - iconWidth) / 2;
            }
            int iconY = (getHeight() - iconHeight)/2;
            Graphics2D g2 = (Graphics2D)imageBuffer.getGraphics();
            g2.drawImage(icon, iconX, iconY, iconWidth, iconHeight, null);
            g2.dispose();

        }

        //Draw Label
        if(getFont() != null) {
            g.setFont(getFont());
        }
        g.setColor(this.getFontColor());

        if(!label.equals("")) {
            int labelX = (getWidth() + iconWidth - textWidth) / 2;
            int labelY = (getHeight() - textHeight) / 2 + g.getFontMetrics().getAscent();
            g.drawString(this.label, labelX, labelY);
        }

        g.dispose();
    }

    public void setRound(Integer round) {
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

    @Override
    public Point2<Integer> getClosedSize() {
        return new Point2<>(getWidth(), getHeight());
    }

    @Override
    public Point2<Integer> getOpenedSize() {
        return getClosedSize();
    }

    @Override
    public void setClosedSize(Integer width, Integer height) {
        setSize(width, height);
    }

    @Override
    public void setOpenedSize(Integer width, Integer height) {
        setSize(width, height);
    }
}
