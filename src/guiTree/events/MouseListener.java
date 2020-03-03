package guiTree.events;

import java.awt.event.MouseEvent;

public interface MouseListener {
    void mouseClicked(MouseEvent mouseEvent);
    void mouseReleased(MouseEvent mouseEvent);
    void mousePressed(MouseEvent mouseEvent);
    void mouseEntered(MouseEvent mouseEvent);
    void mouseExited(MouseEvent mouseEvent);
    void mouseDragged(MouseEvent mouseEvent);
    void mouseMoved(MouseEvent mouseEvent);
}
