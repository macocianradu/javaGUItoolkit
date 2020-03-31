package guiTree;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelGetter implements MouseWheelListener {
    private Window callingWindow;

    public MouseWheelGetter(Window callingWindow) {
        this.callingWindow = callingWindow;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        callingWindow.mouseWheelMoved(mouseWheelEvent);
    }
}
