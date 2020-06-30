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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DropDown extends MenuItem implements Menu{
    private String label;
    private BufferedImage icon;
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
            public void mouseEntered(MouseEvent mouseEvent) {
                if(!isOpen) {
                    addAnimation(new ColorAnimation(DropDown.this, getBackgroundColor(), getAccentColor(), 100));
                    Debugger.log("Calling repaint from entered: " + getName(), Debugger.Tag.PAINTING);
                    update();
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(!isOpen) {
                    addAnimation(new ColorAnimation(DropDown.this, getAccentColor(), getBackgroundColor(), 100));
                    Debugger.log("Calling repaint from exited: " + getName(), Debugger.Tag.PAINTING);
                    update();
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(isOpen) {
                    addAnimation(new ColorAnimation(DropDown.this, getForegroundColor(), getAccentColor(), 100));
                    close();
                    return;
                }
                addAnimation(new ColorAnimation(DropDown.this, getAccentColor(), getForegroundColor(), 100));
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
            if(elementWidth < ((MenuItem) v).getClosedSize().x) {
                setElementWidth(((MenuItem) v).getClosedSize().x);
            }
        }
        else {
            ((MenuItem) v).setClosedWidth(elementWidth);
        }

        if(elementHeightSet) {
            ((MenuItem) v).setClosedHeight(elementHeight);
        }

        v.setLocationX(0);
        openedSize.x = Math.max(openedSize.x, ((MenuItem) v).getOpenedSize().x);
        if(items.size() == 0) {
            v.setLocationY(getHeight());
            openedSize.y = closedSize.y + ((MenuItem) v).getOpenedSize().y;
            items.add((MenuItem) v);
            return;
        }

        v.setLocationY(items.get(items.size() - 1).getLocationY() + items.get(items.size() - 1).getClosedSize().y);
        openedSize.y = Math.max(openedSize.y, v.getLocationY() + ((MenuItem) v).getOpenedSize().y);

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
            if(((MenuItem) v).getClosedSize().x == elementWidth) {
                setElementWidth(items.stream().mapToInt(f -> f.getClosedSize().x).max().orElse(0));
            }
        }

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
        openedSize.x = width;
        openedSize.y = height;
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

    @Override
    public Visual findByName(String name) {
        if(getName().equals(name)){
            return this;
        }
        else{
            for(Visual child: items){
                Visual visual;
                visual = child.findByName(name);
                if(visual != null){
                    return visual;
                }
            }
        }
        return null;
    }

    public void open() {
        System.out.println("Opening");
        isOpen = true;
        items.forEach(super::addVisual);
        addAnimation(new SizeAnimation(this, new Point2<>(getWidth(), getHeight()), openedSize, 70));
    }

    public void close() {
        System.out.println("Closing");
        isOpen = false;
        items.forEach(super::removeVisual);
        addAnimation(new SizeAnimation(this, new Point2<>(getWidth(), getHeight()), closedSize, 70));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIcon(String url) {
        try{
            InputStream iconStream = getClass().getClassLoader().getResourceAsStream("icons/" + url + ".png");
            assert iconStream != null;
            icon = ImageIO.read(iconStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getIcon() {
        return icon;
    }

    @Override
    public void paint(Image imageBuffer) {
        //Get Graphics
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
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
            Graphics2D g2 = (Graphics2D)imageBuffer.getGraphics();
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
