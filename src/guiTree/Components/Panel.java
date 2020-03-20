package guiTree.Components;

import guiTree.Visual;

import java.util.ArrayList;
import java.util.List;

public class Panel extends Visual {
    List<Visual> visuals;
    private Boolean overlapping;

    public Panel() {
        super();
        overlapping = false;
        visuals = new ArrayList<>();
    }

    public void setOverlapping(Boolean overlapping) {
        this.overlapping = overlapping;
        reposition();
    }

    public Boolean getOverlapping() {
        return this.overlapping;
    }

    private void calculateSize(Visual v) {

    }

    private void calculatePosition(Visual v) {
        if(!overlapping) {
            boolean ok = false;
            while(!ok) {
                ok = true;
                for (int i = 0; i < visuals.size(); i++) {
                    Visual v2 = visuals.get(i);
                    if (isOverlapping(v, v2) && v != v2) {
                        System.out.println(v + " Overlapping with: " + v2);
                        if(v.getLocationX() + v.getWidth() + v2.getWidth() > this.getWidth()) {
                            if(v.getLocationY() + v.getHeight() + v2.getHeight() > this.getHeight()) {
                                break;
                            }
                            else {
                                v.setLocationY(v2.getLocationY() + v2.getHeight());
                            }
                        }
                        else {
                            v.setLocationX(v2.getLocationX() + v2.getWidth());
                        }
                        i = 0;
                    }
                }
                if(v.getLocationX() + v.getWidth() > this.getWidth()) {
                    ok = false;
                    v.setLocationY(v.getLocationY() + 10);
                }
                if(v.getLocationY() > this.getHeight()) {
                    v.setLocation(0, 0);
                    break;
                }
            }

        }
    }

    private void reposition() {
        for(int i = visuals.size() - 1; i >= 0; i--) {
            calculatePosition(visuals.get(i));
        }
    }

    @Override
    public void addVisual(Visual v) {
        calculatePosition(v);
        super.addVisual(v);
        visuals.add(v);
    }

    private Boolean isOverlapping(Visual v1, Visual v2) {
        int l1x = v1.getLocationX();
        int r1x = v1.getLocationX() + v1.getWidth();
        int l1y = v1.getLocationY();
        int r1y = v1.getLocationY() + v1.getHeight();
        int l2x = v2.getLocationX();
        int r2x = v2.getLocationX() + v2.getWidth();
        int l2y = v2.getLocationY();
        int r2y = v2.getLocationY() + v2.getHeight();

        if(l1x >= r2x || l2x >= r1x) {
            return  false;
        }

        return l1y < r2y && l2y < r1y;
    }

    @Override
    public void handleNotification(int notify) {
        if(notify == TitleBar.CLOSE || notify == TitleBar.MAXIMIZE ||
            notify == TitleBar.MINIMIZE || notify == TitleBar.NORMALIZE) {
            notifyParent(notify);
//            return;
        }
//        if(notify == SIZE_CHANGED || notify == LOCATION_CHANGED) {
//            reposition();
//        }
    }
}
