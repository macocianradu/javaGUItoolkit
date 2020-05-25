package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import java.awt.event.MouseEvent;

public class SideDropDown extends DropDown implements Menu {
    private int elementCount;

    public SideDropDown() {
        super();
        setIcon("arrow_right_black");
        elementCount = 0;

        removeAllMouseListeners();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if(!isOpen()) {
                    open();
                    addAnimation(new ColorAnimation(SideDropDown.this, getBackgroundColor(), getForegroundColor(), 70));
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(isInside(mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen())) {
                    return;
                }
                System.out.println("Exited somehow");
                close();
                addAnimation(new ColorAnimation(SideDropDown.this, getForegroundColor(), getBackgroundColor(), 70));
            }
        });
    }

    public void addVisual(Visual v) {
        super.addVisual(v);

        v.setLocationX(getClosedSize().x - 1);
        v.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(!isInside(mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen())) {
                    close();
                    addAnimation(new ColorAnimation(SideDropDown.this, getForegroundColor(), getBackgroundColor(), 70));
                }
            }
        });
        setOpenedWidth(Math.max(getOpenedSize().x, getClosedSize().x + ((MenuItem) v).getOpenedSize().x));
        if(elementCount == 0) {
            v.setLocationY(0);
            setOpenedHeight(Math.max(getOpenedSize().y, v.getLocationY() + ((MenuItem) v).getOpenedSize().y));
            elementCount++;
            return;
        }

        elementCount++;
    }

    public void removeVisual(Visual v) {
        super.removeVisual(v);
        elementCount--;
    }

    @Override
    public boolean isInside(int x, int y) {
        x = x - getAbsoluteX();
        y = y - getAbsoluteY();
        if(isOpen()) {
            return x > 0 && y > 0 && x < getOpenedSize().x && y < getOpenedSize().y &&
                    (y < getClosedSize().y || x > getClosedSize().x);
        }
        return x > 0 && y > 0 && x < getClosedSize().x && y < getClosedSize().y;
    }
}
