package guiTree;

import guiTree.events.MouseListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Button extends Visual {
    private String label;

    public Button() {
        this("");
    }

    public Button(String label) {
        super();
        this.label = label;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(getBackgroundColor() == Color.BLACK) {
                    setBackgroundColor(Color.RED);
                }
                else {
                    setBackgroundColor(Color.BLACK);
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });
    }

    @Override
    public void paint(BufferedImage imageBuffer)
    {
        Graphics g = imageBuffer.getGraphics();
        g.setColor(this.getBackgroundColor());
        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 50, 50);
        g.setColor(this.getForegroundColor());
        g.drawString(this.label, this.getWidth()/2, this.getHeight()/2);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
