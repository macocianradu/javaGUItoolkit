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
        callingWindow.mouseDragged(mouseEvent, 0, 0);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        callingWindow.mouseMoved(mouseEvent, 0, 0);
        System.out.println("MOVED");
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        callingWindow.mouseClicked(mouseEvent, 0, 0);
        System.out.println("CLICKED");
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        callingWindow.mousePressed(mouseEvent, 0, 0);
        System.out.println("PRESSED");
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        callingWindow.mouseReleased(mouseEvent, 0, 0);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        callingWindow.mouseEntered(mouseEvent, 0, 0);
        System.out.println("ENTERED");
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        callingWindow.mouseExited(mouseEvent, 0, 0);
        System.out.println("EXITED");
    }
}
