package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Animations.SizeAnimation;
import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideDropDown extends MenuItem implements Menu {
    private List<MenuItem> items;
    private boolean isOpen;
    private boolean elementHeightSet;
    private boolean elementWidthSet;
    private int elementHeight;
    private int elementWidth;
    private Point2<Integer> closedSize;
    private Point2<Integer> openedSize;
    private String label;
    private BufferedImage icon;

    public SideDropDown() {
        items = new ArrayList<>();
        isOpen = false;
        elementHeight = 0;
        elementWidth = 0;
        closedSize = new Point2<>(0, 0);
        openedSize = new Point2<>(0, 0);
        setIcon("arrow_right_black");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                addAnimation(new ColorAnimation(SideDropDown.this, getBackgroundColor(), getForegroundColor(), 100));
                open();
                update();
                Debugger.log("Calling repaint from entered: " + getName(), Debugger.Tag.PAINTING);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                addAnimation(new ColorAnimation(SideDropDown.this, getForegroundColor(), getBackgroundColor(), 100));
                close();
                update();
                Debugger.log("Calling repaint from exited: " + getName(), Debugger.Tag.PAINTING);
            }
        });
    }

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

        v.setLocationX(closedSize.x);
        if(items.size() == 0) {
            v.setLocationY(0);
            openedSize.y = closedSize.y + ((MenuItem) v).getOpenedHeight();
            items.add((MenuItem) v);
            return;
        }

        v.setLocationY(items.get(items.size() - 1).getLocationY() + items.get(items.size() - 1).getClosedHeight());
        openedSize.y = Math.max(openedSize.y, v.getLocationY() + ((MenuItem) v).getOpenedHeight());
        openedSize.x = Math.max(openedSize.x, closedSize.x + ((MenuItem) v).getOpenedWidth());

        items.add((MenuItem)v);
    }

    @Override
    public void removeVisual(Visual v) {
        if(!(v instanceof MenuItem)) {
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
        if(isOpen) {
            super.removeVisual(v);
        }
    }

    @Override
    public Point2<Integer> getClosedSize() {
        return closedSize;
    }

    @Override
    public Point2<Integer> getOpenedSize() {
        return openedSize;
    }

    public void setClosedSize(Integer width, Integer height) {
        setClosedWidth(width);
        setClosedHeight(height);
    }

    @Override
    public void setOpenedSize(Integer width, Integer height) {
        setOpenedHeight(height);
        setOpenedWidth(width);
    }

    @Override
    public int getClosedWidth() {
        return closedSize.x;
    }

    @Override
    public int getClosedHeight() {
        return closedSize.y;
    }

    @Override
    public int getOpenedWidth() {
        return openedSize.x;
    }

    @Override
    public int getOpenedHeight() {
        return openedSize.y;
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

    public void setContentWidth(Integer width) {
        if(width < 0) {
            elementWidthSet = false;
            return;
        }
        elementWidth = width;
        elementWidthSet = true;
        items.forEach(f -> f.setWidth(width));
    }

    public void setContentHeight(Integer height) {
        if(height < 0) {
            elementHeightSet = false;
            return;
        }
        elementHeight = height;
        elementHeightSet = true;
        items.forEach(f -> {
            f.setLocationY(items.indexOf(f) * elementHeight);
            f.setHeight(height);
        });
    }

    public void open() {
        addAnimation(new SizeAnimation(this, new Point2<>(closedSize.x, closedSize.y), new Point2<>(openedSize.x, openedSize.y), 70));
        for(Visual v: items) {
            super.addVisual(v);
        }
        isOpen = true;
    }

    public void close() {
        addAnimation(new SizeAnimation(this, new Point2<>(openedSize.x, openedSize.y), new Point2<>(closedSize.x, closedSize.y), 70));
        for(Visual v: items) {
            super.removeVisual(v);
        }
        isOpen = false;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public void setIcon(String url) {
        try{
            icon = ImageIO.read(new File("resources\\icons\\" + url + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getIcon() {
        return icon;
    }

    private void setElementWidth(Integer width) {
        elementWidth = width;
        items.forEach(f -> f.setClosedWidth(width));
    }

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


        //Draw Icon
        if(icon != null) {
            int iconWidth = icon.getWidth();
            int iconHeight = icon.getHeight();
            textWidth += iconWidth;

            int iconX = closedSize.x - iconWidth - 3;
            int iconY = (closedSize.y - iconHeight - textHeight) / 2;
            Graphics2D g2 = imageBuffer.createGraphics();
            g2.drawImage(icon, iconX, iconY, null);
            g2.dispose();
        }
        if(!label.equals("")) {
            textWidth += g.getFontMetrics().stringWidth(label);
            textHeight = g.getFontMetrics().getHeight();
            g.drawString(label, (closedSize.x - textWidth)/2, closedSize.y/2 + textHeight/2);
        }

        g.dispose();
    }
}
