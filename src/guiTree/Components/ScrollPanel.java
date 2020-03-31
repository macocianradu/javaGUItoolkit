package guiTree.Components;

import guiTree.Animations.LocationAnimation;
import guiTree.Helper.Point2;
import guiTree.Visual;
import guiTree.events.MouseAdapter;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ScrollPanel extends Visual {
    private List<VisualLocation> children;

    private float positionX;
    private float positionY;
    private float ratioX;
    private float ratioY;
    private Slider verticalScrollBar;
    private Slider horizontalScrollBar;

    public ScrollPanel() {
        super();
        setName("ScrollPanel");
        children = new ArrayList<>();
        addMouseListener(new BarListener());
        addMouseWheelListener(new MouseWheelListener());
    }

    @Override
    public void setSize() {
        super.setSize();
        if (getFarthestX() > getWidth()) {
            if (horizontalScrollBar == null) {
                horizontalScrollBar = new Slider(Slider.Direction.Horizontal);
                horizontalScrollBar.setName(getName() + " horizontal scroll bar");
                super.addVisual(horizontalScrollBar);
                horizontalScrollBar.setWidth(1.0f);
                horizontalScrollBar.setHeight(20);
            }
            horizontalScrollBar.setSliderSize((float)getWidth() / getFarthestX());
            horizontalScrollBar.setLocation(0, getHeight());
        }

        if (getFarthestY() > getHeight()) {
            if (verticalScrollBar == null) {
                verticalScrollBar = new Slider(Slider.Direction.Vertical);
                verticalScrollBar.setName(getName() + " vertical scroll bar");
                super.addVisual(verticalScrollBar);
                verticalScrollBar.setHeight(1.0f);
                verticalScrollBar.setWidth(20);
            }
            verticalScrollBar.setSliderSize((float)getHeight() / getFarthestY());
            verticalScrollBar.setLocation(getWidth(), 0);
        }
    }

    @Override
    public void addVisual(Visual v) {
        if(v.getWidth() + v.getLocationX() > getFarthestX()) {
            if (v.getWidth() + v.getLocationX() > getWidth()) {
                if (horizontalScrollBar == null) {
                    horizontalScrollBar = new Slider(Slider.Direction.Horizontal);
                    horizontalScrollBar.setName(getName() + " horizontal scroll bar");
                    horizontalScrollBar.setWidth(1.0f);
                    horizontalScrollBar.setHeight(20);
                    super.addVisual(horizontalScrollBar);
                }
                horizontalScrollBar.setSliderSize((float)getWidth() / v.getWidth() + v.getLocationX());
            }
        }
        if(v.getHeight() + v.getLocationY() > getFarthestY()) {
            if (v.getHeight() + v.getLocationY() > getHeight()) {
                if (verticalScrollBar == null) {
                    verticalScrollBar = new Slider(Slider.Direction.Vertical);
                    verticalScrollBar.setName(getName() + " vertical scroll bar");
                    verticalScrollBar.setHeight(1.0f);
                    verticalScrollBar.setWidth(20);
                    super.addVisual(verticalScrollBar);
                }
                verticalScrollBar.setSliderSize((float)getHeight() / v.getHeight() + v.getLocationY());
            }
        }

        super.addVisual(v);
        children.add(new VisualLocation(v));
    }

    private int getFarthestX() {
        int max = 0;
        for(VisualLocation visualLocation: children) {
            if(max < visualLocation.v.getWidth() + visualLocation.originalLocation.x) {
                max = visualLocation.v.getWidth() + visualLocation.originalLocation.x;
            }
        }
        return max;
    }

    private int getFarthestY() {
        int max = 0;
        for(VisualLocation visualLocation: children) {
            if(max < visualLocation.v.getHeight() + visualLocation.originalLocation.y) {
                max = visualLocation.v.getHeight() + visualLocation.originalLocation.y;
            }
        }
        return max;
    }

    private void setLocations() {
        for(VisualLocation visualLocation:children) {
            visualLocation.v.setLocation(visualLocation.originalLocation.x - Math.round(horizontalScrollBar.getSliderLocation() * (getFarthestX() - getWidth() + 20)),
                                            visualLocation.originalLocation.y - Math.round(verticalScrollBar.getSliderLocation() * (getFarthestY() - getHeight() + 20)));
            System.out.println("Moved: " + visualLocation.v + " from x: " + visualLocation.originalLocation + " to " + visualLocation.v.getLocation());
        }
    }

    @Override
    public void handleNotification(int notify) {
        if(notify == Slider.SLIDER_MOVED) {
            setLocations();
        }
    }

    @Override
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();
        g.setColor(getPaintColor());
        if(getHasBorder()) {
            g.fillRect(1, 1, getWidth() - 1, getHeight() - 1);
            g.setColor(getBorderColor());
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        else {
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        g.dispose();
    }

    private class BarListener extends MouseAdapter {
        private boolean outHorizontal = false;
        private boolean outVertical = false;
        private LocationAnimation outAnimationHorizontal;
        private LocationAnimation outAnimationVertical;
        private LocationAnimation inAnimationHorizontal;
        private LocationAnimation inAnimationVertical;

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            if(verticalScrollBar != null) {
                if (mouseEvent.getX() > getWidth() - verticalScrollBar.getWidth() && mouseEvent.getY() > verticalScrollBar.getLocationY() && mouseEvent.getY() < verticalScrollBar.getHeight()) {
                    if (!outVertical) {
                        outAnimationVertical = new LocationAnimation(verticalScrollBar, verticalScrollBar.getLocation(), new Point2<>(getWidth() - verticalScrollBar.getWidth(), verticalScrollBar.getLocationY()), 300);
                        removeAnimation(inAnimationVertical);
                        addAnimation(outAnimationVertical);
                        outVertical = true;
                    }
                } else {
                    if (outVertical) {
                        inAnimationVertical = new LocationAnimation(verticalScrollBar, verticalScrollBar.getLocation(), new Point2<>(getWidth(), verticalScrollBar.getLocationY()), 300);
                        removeAnimation(outAnimationVertical);
                        addAnimation(inAnimationVertical);
                        outVertical = false;
                    }
                }
            }

            if(horizontalScrollBar != null) {
                if (mouseEvent.getY() > getHeight() - horizontalScrollBar.getHeight() && mouseEvent.getX() > horizontalScrollBar.getLocationX() && mouseEvent.getX() < horizontalScrollBar.getWidth()) {
                    if (!outHorizontal) {
                        outAnimationHorizontal = new LocationAnimation(horizontalScrollBar, horizontalScrollBar.getLocation(), new Point2<>(horizontalScrollBar.getLocationX(), getHeight() - horizontalScrollBar.getHeight()), 300);
                        removeAnimation(inAnimationHorizontal);
                        addAnimation(outAnimationHorizontal);
                        outHorizontal = true;
                    }
                } else {
                    if (outHorizontal) {
                        inAnimationHorizontal = new LocationAnimation(horizontalScrollBar, horizontalScrollBar.getLocation(), new Point2<>(horizontalScrollBar.getLocationX(), getHeight()), 300);
                        removeAnimation(outAnimationHorizontal);
                        addAnimation(inAnimationHorizontal);
                        outHorizontal = false;
                    }
                }
            }
        }
    }

    private class MouseWheelListener extends MouseAdapter {
        @Override
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            if(mouseWheelEvent.isShiftDown()) {
                horizontalScrollBar.moveSlider(mouseWheelEvent.getWheelRotation() * 10);
                return;
            }
            verticalScrollBar.moveSlider(mouseWheelEvent.getWheelRotation() * 10);
        }
    }

    private static class VisualLocation {
        Visual v;
        Point2<Integer> originalLocation;

        public VisualLocation(Visual v) {
            this.v = v;
            originalLocation = v.getLocation();
        }
    }
}
