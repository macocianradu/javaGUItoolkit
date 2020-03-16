package guiTree;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class MouseEventGetter implements MouseInputListener {
    private Window callingWindow;

    public MouseEventGetter(Window callingWindow) {
        this.callingWindow = callingWindow;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        callingWindow.mouseDragged(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        callingWindow.mouseMoved(mouseEvent, 0, 0);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        callingWindow.mouseClicked(mouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        callingWindow.mousePressed(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        callingWindow.mouseReleased(mouseEvent);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        callingWindow.mouseEntered(mouseEvent, 0, 0);
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        callingWindow.mouseExited(mouseEvent);
    }
}
