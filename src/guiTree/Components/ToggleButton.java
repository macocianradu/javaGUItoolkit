package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Helper.Debugger;
import guiTree.events.MouseAdapter;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
public class ToggleButton extends Button {
    private Boolean pressed;

    public ToggleButton() {
        this("", null);
    }

    public ToggleButton(String text) {
        this(text, null);
    }

    public ToggleButton(BufferedImage icon) {
        this("", icon);
    }

    public ToggleButton(String label, BufferedImage icon) {
        super(label, icon);
        removeAllMouseListeners();
        pressed = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(pressed) {
                    addAnimation(new ColorAnimation(ToggleButton.this, getForegroundColor(), getAccentColor(), 100));
                }
                else {
                    addAnimation(new ColorAnimation(ToggleButton.this, getAccentColor(), getForegroundColor(), 100));
                }
                pressed = !pressed;

                Debugger.log("Pressed: " + getName(), Debugger.Tag.LISTENER);
                Debugger.log("Calling repaint from pressed: " + getName(), Debugger.Tag.PAINTING);
                update();
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if(!pressed) {
                    addAnimation(new ColorAnimation(ToggleButton.this, getBackgroundColor(), getAccentColor(), 100));
                }
                Debugger.log("Calling repaint from entered: " + getName(), Debugger.Tag.PAINTING);
                update();
            }
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(!pressed) {
                    addAnimation(new ColorAnimation(ToggleButton.this, getAccentColor(), getBackgroundColor(), 100));
                }
                Debugger.log("Calling repaint from exited: " + getName(), Debugger.Tag.PAINTING);
                update();
            }
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Debugger.log("Calling repaint from moved: " + getName(), Debugger.Tag.PAINTING);
            }
        });
    }

    public void setPressed(Boolean pressed) {
        this.pressed = pressed;
        if(!pressed) {
            addAnimation(new ColorAnimation(ToggleButton.this, getPaintColor(), getBackgroundColor(), 100));
        }
        else {
            addAnimation(new ColorAnimation(ToggleButton.this, getPaintColor(), getForegroundColor(), 100));
        }
    }

    public Boolean getPressed() {
        return pressed;
    }
}
