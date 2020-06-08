package guiTree.Components;

import guiTree.Helper.Point2;
import guiTree.Visual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Panel extends Visual {
    HashMap<Visual, Point2<Integer>> visuals;
    private Boolean overlapping;

    public Panel() {
        super();
        overlapping = true;
        visuals = new HashMap<>();
    }

    public void setOverlapping(Boolean overlapping) {
        this.overlapping = overlapping;
        reposition();
    }

    public Boolean getOverlapping() {
        return this.overlapping;
    }

    private void reposition() {
        if(!overlapping) {
            return;
        }
        visuals.keySet().forEach(f -> f.setLocation(visuals.get(f).x, visuals.get(f).y));
        visuals.keySet().forEach(this::calculatePosition);
    }

    private void calculatePosition(Visual v) {
        if(overlapping) {
            return;
        }
        boolean ok = false;
        while(!ok) {
            ok = true;
            for(Visual v2: visuals.keySet()) {
                if(v == v2) {
                    continue;
                }
                if(v2.isInside(v.getLocationX(), v.getLocationY()) ||
                    v2.isInside(v.getLocationX() + v.getWidth(), v.getLocationY()) ||
                    v2.isInside(v.getLocationX(), v.getLocationY() + v.getHeight()) ||
                    v2.isInside(v.getLocationX() + v.getWidth(), v.getLocationY() + v.getHeight())) {
                    System.out.println(v + " Overlapping with: " + v2);
                    ok = false;
                    if(v2.getHeight() + v2.getLocationY() + v.getHeight() > getHeight()) {
                        v.setLocation(v2.getLocationX() + v2.getWidth() + 1, visuals.get(v).y);
                    }
                    v.setLocation(visuals.get(v).x, v2.getLocationY() + v2.getHeight() + 1);
                }
            }
        }
    }

    @Override
    public void removeVisual(Visual v) {
        super.removeVisual(v);
        visuals.remove(v);
    }

    @Override
    public void addVisual(Visual v) {
        super.addVisual(v);
        visuals.put(v, new Point2<>(v.getLocationX(), v.getLocationY()));
        calculatePosition(v);
    }

    @Override
    public void handleNotification(Visual v, int notify) {
        if(notify == TitleBar.CLOSE || notify == TitleBar.MAXIMIZE ||
            notify == TitleBar.MINIMIZE || notify == TitleBar.NORMALIZE) {
            notifyParent(v, notify);
        }
//        if(notify == SIZE_CHANGED) {
//            reposition();
//        }
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();

        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        g.dispose();
    }
}
