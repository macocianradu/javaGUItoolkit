package guiTree.events;

import guiTree.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventGetter implements KeyListener {
    public Window callingWindow;

    public KeyEventGetter(Window callingWindow) {
        this.callingWindow = callingWindow;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
