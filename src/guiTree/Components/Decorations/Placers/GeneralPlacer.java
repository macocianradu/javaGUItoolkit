package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;
import guiTree.Helper.Point4;

public class GeneralPlacer implements Placer {
    private Point2<Integer> location;
    private Point2<Integer> size;
    private Point2<Integer> parentSize;
    private Point2<Float> relativeLocation;
    private Point4<Integer> margin;

    public GeneralPlacer() {
        margin = new Point4<>(0, 0, 0, 0);
    }

    @Override
    public void setRelativeLocation(float x, float y) {
        relativeLocation = new Point2<>(x, y);
    }

    @Override
    public void setLocation(int x, int y) {
        location = new Point2<>(x, y);
    }

    @Override
    public void setElementSize(int width, int height) {
        size = new Point2<>(width, height);
    }

    @Override
    public void setParentSize(int width, int height) {
        parentSize = new Point2<>(width, height);
    }

    @Override
    public void setMargins(int up, int down, int left, int right) {
        margin.a = up;
        margin.b = down;
        margin.c = left;
        margin.d = right;
    }

    @Override
    public void setMargins(int margin) {
        this.margin.a = margin;
        this.margin.b = margin;
        this.margin.c = margin;
        this.margin.d = margin;
    }

    @Override
    public Point4<Integer> getMargins() {
        return margin;
    }

    @Override
    public Point2<Integer> getPosition() {
        int x = location.x;
        int y = location.y;
        if(parentSize != null) {
            if(relativeLocation != null) {
                if(relativeLocation.x != -1) {
                    x = Math.round(relativeLocation.x * parentSize.x);
                }
                if(relativeLocation.y != -1) {
                    y = Math.round(relativeLocation.y * parentSize.y);
                }
            }
        }
        return new Point2<>(x, y);
    }
}
