package guiTree.events;

import java.awt.event.KeyEvent;

public interface KeyListener {
    void keyTyped(KeyEvent keyEvent);
    void keyPressed(KeyEvent keyEvent);
    void keyReleased(KeyEvent keyEvent);
}
