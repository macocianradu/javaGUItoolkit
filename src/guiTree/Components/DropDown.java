package guiTree.Components;

import guiTree.Animations.SizeAnimation;
import guiTree.Helper.Point2;
import guiTree.Visual;
import guiTree.events.MouseAdapter;
import javafx.css.Size;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DropDown extends ToggleButton implements Menu{
    private List<MenuItem> items;
    private boolean isOpen;
    private boolean elementWidthSet;
    private boolean elementHeightSet;
    private int elementHeight;
    private int elementWidth;
    private Point2<Integer> closedSize;
    private Point2<Integer> openedSize;

    public DropDown() {
        super();
        items = new ArrayList<>();
        isOpen = false;
        elementHeight = 0;
        elementWidth = 0;
        elementWidthSet = false;
        elementHeightSet = false;
        closedSize = new Point2<>(getWidth(), getHeight());
        openedSize = new Point2<>(getWidth(), getHeight());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(isOpen) {
                    close();
                    return;
                }
                open();
            }
        });
    }

    @Override
    public void addVisual(Visual v) {
        if(!(v instanceof MenuItem)) {
            System.err.println("Trying to add incompatible type to menu");
            return;
        }

        if(!elementWidthSet) {
            if(elementWidth < ((MenuItem) v).getClosedWidth()) {
                setElementWidth(((MenuItem) v).getClosedWidth());
            }
        }
        else {
            ((MenuItem) v).setClosedWidth(elementWidth);
        }

        if(elementHeightSet) {
            ((MenuItem) v).setClosedHeight(elementHeight);
        }

        v.setLocationX(0);
        openedSize.x = Math.max(openedSize.x, ((MenuItem) v).getOpenedWidth());
        if(items.size() == 0) {
            v.setLocationY(getHeight());
            openedSize.y = closedSize.y + ((MenuItem) v).getOpenedHeight();
            items.add((MenuItem) v);
            return;
        }

        v.setLocationY(items.get(items.size() - 1).getLocationY() + items.get(items.size() - 1).getClosedHeight());
        openedSize.y = Math.max(openedSize.y, v.getLocationY() + ((MenuItem) v).getOpenedHeight());

        items.add((MenuItem)v);
    }

    @Override
    public void removeVisual(Visual v) {
        if(!(v instanceof MenuItem)){
            return;
        }
        if(!items.contains(v)) {
            return;
        }

        int removeIndex = items.indexOf(v);
        for(int i = removeIndex; i < items.size(); i++) {
            items.get(i).setLocationY(items.get(i).getLocationY() - v.getHeight());
        }
        items.remove(v);
        openedSize.y -= v.getHeight();

        if(!elementWidthSet) {
            if(((MenuItem) v).getClosedWidth() == elementWidth) {
                setElementWidth(items.stream().mapToInt(MenuItem::getClosedWidth).max().orElse(0));
            }
        }

        if(isOpen) {
            super.removeVisual(v);
        }
    }

    public void setClosedSize(Integer width, Integer height) {
        setClosedWidth(width);
        setClosedHeight(height);
    }

    public void setClosedHeight(Integer height) {
        closedSize.y = height;
        if(openedSize.y < height) {
            openedSize.y = height;
        }
        if(!isOpen) {
            setHeight(height);
        }
    }

    public void setClosedWidth(Integer width) {
        closedSize.x = width;
        if(openedSize.x < width) {
            openedSize.x = width;
        }
        if(!isOpen) {
            setWidth(width);
        }
    }

    public void setContentSize(Integer width, Integer height) {
        setContentWidth(width);
        setContentHeight(height);
    }

    private void setElementWidth(Integer width) {
        elementWidth = width;
        items.forEach(f -> f.setClosedWidth(width));
    }

    public void setContentWidth(Integer width) {
        if(width < 0) {
            elementWidthSet = false;
            return;
        }
        elementWidth = width;
        items.forEach(f -> f.setClosedWidth(width));
        elementWidthSet = true;
    }

    public void setContentHeight(Integer height) {
        if(height < 0) {
            elementHeightSet = false;
            return;
        }
        elementHeight = height;
        items.forEach(f -> {
            f.setLocationY(items.indexOf(f) * elementHeight);
            f.setClosedHeight(height);
        });
        elementHeightSet = true;
    }

    public void open() {
        isOpen = true;
        items.forEach(super::addVisual);
        addAnimation(new SizeAnimation(this, closedSize, openedSize, 70));
    }

    public void close() {
        isOpen = false;
        items.forEach(super::removeVisual);
        addAnimation(new SizeAnimation(this, openedSize, closedSize, 70));
    }

//    public void open() {
//        int width = elementWidth;
//        int height = items.stream().mapToInt(f -> f.getOpenedHeight() + f.getLocationY()).max().orElse(0);
//
//        if(width == 0) {
//            width = items.stream().mapToInt(MenuItem::getClosedWidth).max().orElse(0);
//        }
//        int finalWidth = width;
//        items.forEach(f -> f.setClosedWidth(finalWidth));
//        int openedWidth = items.stream().mapToInt(MenuItem::getOpenedWidth).max().orElse(0);
//
//        addAnimation(new SizeAnimation(this, new Point2<>(getWidth(), getHeight()), new Point2<>(Math.max(width, openedWidth), height), 70));
//        for(Visual v: items) {
//            super.addVisual(v);
//        }
//        isOpen = true;
//    }
//
//    public void close() {
//        addAnimation(new SizeAnimation(this, new Point2<>(getWidth(), getHeight()), new Point2<>(closedWidth, closedHeight), 70));
//        isOpen = false;
//    }

//    @Override
//    public void handleNotification(Visual v, int notify) {
//        assert v instanceof MenuItem;
//        MenuItem item = (MenuItem) v;
//        if(notify == SIZE_CHANGED) {
//            if(isOpen) {
//                if(elementWidthSet) {
//                    item.setClosedWidth(elementWidth);
//                }
//                else {
//                    if(item.getClosedWidth() > elementWidth) {
//                        setElementWidth(item.getClosedWidth());
//                    }
//                }
//                if(elementHeightSet) {
//                    item.setClosedHeight(elementHeight);
//                }
//            }
//        }
//    }

    public void paint(BufferedImage imageBuffer)
    {
        //Get Graphics
        Graphics2D g = imageBuffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getPaintColor());

        //Draw Button
        g.fillRect(0, 0, closedSize.x, closedSize.y);

        //Draw Label
        if(getFont() != null) {
            g.setFont(getFont());
        }
        g.setColor(this.getFontColor());
        int textWidth = 0;
        int textHeight = 0;
        if(!getLabel().equals("")) {
            textWidth = g.getFontMetrics().stringWidth(getLabel());
            textHeight = g.getFontMetrics().getHeight();
        }

        g.drawString(getLabel(), (getWidth() - textWidth)/2, (closedSize.y + textHeight)/2);

        //Draw Icon
        if(getIcon() != null) {
            int iconWidth = getIcon().getWidth();
            int iconHeight = getIcon().getHeight();

            int iconX = (getWidth() - iconWidth - textWidth) / 2;
            int iconY = (closedSize.y - iconHeight - textHeight) / 2;
            Graphics2D g2 = imageBuffer.createGraphics();
            g2.drawImage(getIcon(), iconX, iconY, null);
            g2.dispose();
        }

        g.dispose();
    }
}
