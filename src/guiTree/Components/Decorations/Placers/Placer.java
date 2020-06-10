package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;
import guiTree.Helper.Point4;

public abstract class Placer {
    Point2<Integer> location;
    Point2<Integer> size;
    Point2<Integer> parentSize;
    Point2<Float> relativeLocation;
    Point4<Integer> margin;

    public Placer() {
        margin = new Point4<>(0, 0, 0, 0);
    }

    public void setRelativeLocation(float x, float y) {
        relativeLocation = new Point2<>(x, y);
    }

    public void setLocation(int x, int y) {
        location = new Point2<>(x, y);
    }

    public Point2<Integer> getLocation() {
        return location;
    }

    public Point2<Float> getRelativeLocation() {
        return relativeLocation;
    }

    public void setElementSize(int width, int height) {
        size = new Point2<>(width, height);
    }

    public void setParentSize(int width, int height) {
        parentSize = new Point2<>(width, height);
    }

    public void setMargins(int up, int down, int left, int right) {
        margin.a = up;
        margin.b = down;
        margin.c = left;
        margin.d = right;
    }

    public void setMargins(int margin) {
        this.margin.a = margin;
        this.margin.b = margin;
        this.margin.c = margin;
        this.margin.d = margin;
    }

    public Point4<Integer> getMargins() {
        return margin;
    }

    abstract public Point2<Integer> getPosition();
}
