package guiTree.Components;

import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CheckBox extends ToggleButton {
    public static int CHECKBOX_CLICKED = 3;

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
        super(text);
        setPressed(value);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                notifyParent(CHECKBOX_CLICKED);
            }
        });
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();

        g.setColor(getPaintColor());

        g.fillRect(1, 1, getHeight() - 1, getHeight() - 1);

        if(getPressed()) {

            if(getIcon() != null) {
                int iconWidth = getIcon().getWidth();
                int iconHeight = getIcon().getHeight();

                int iconX = (getHeight() - iconWidth) / 2;
                int iconY = (getHeight() - iconHeight) / 2;
                Graphics2D g2 = (Graphics2D)imageBuffer.getGraphics();
                g2.drawImage(getIcon(), iconX, iconY, null);
                g2.dispose();
            }
        }

        g.setColor(getFontColor());
        if(getFont() != null) {
            g.setFont(getFont());
        }
        int textHeight = g.getFontMetrics().getHeight();
        g.drawString(getLabel(), getHeight() + 10, getHeight() / 2 + textHeight / 4);

        g.dispose();
    }
}
