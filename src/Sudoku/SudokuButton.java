package Sudoku;

import guiTree.Animations.ColorAnimation;
import guiTree.Components.ToggleButton;
import guiTree.events.KeyAdapter;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SudokuButton extends ToggleButton {
    private int number;
    private boolean entered;
    private boolean fixed;
    private boolean toggle;

    public SudokuButton(Boolean fixed) {
        super();
        this.fixed = fixed;
        if(!fixed) {
            addKeyListener(new SudokuKeyListener());
        }
    }

    private class SudokuKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if(keyEvent.getKeyCode() >= 48 && keyEvent.getKeyCode() <= 57) {
                setLabel(String.valueOf(keyEvent.getKeyCode() - 48));
            }
            else {
                addAnimation(new ColorAnimation(SudokuButton.this, Color.RED, getBackgroundColor(), 100));
            }
            setPressed(false);
            update();
        }
    }
}
