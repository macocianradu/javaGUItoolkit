package guiTree.Components;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RadioButton extends CheckBox {
    public RadioButton() {
        super();
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getPaintColor());

        g.drawOval(1, 1, getHeight() - 2, getHeight() - 2);

        if(isMarked()) {
            if(getHeight() > 9) {
                g.fillOval(5, 5, getHeight() - 9, getHeight() - 9);
            }
        }

        g.setColor(getFontColor());
        if(getFont() != null) {
            g.setFont(getFont());
        }
        int textHeight = g.getFontMetrics().getHeight();
        g.drawString(getText(), getHeight() + 10, getHeight() / 2 + textHeight / 4);

        g.dispose();
    }
}
