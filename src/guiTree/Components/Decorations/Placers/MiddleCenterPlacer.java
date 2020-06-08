package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;
import guiTree.Helper.Point4;

public class MiddleCenterPlacer implements Placer {
    private Point2<Integer> size;
    private Point2<Integer> parentSize;
    private Point4<Integer> margin;

    public MiddleCenterPlacer() {
        margin = new Point4<>(0, 0, 0, 0);
    }

    @Override
    public void setRelativeLocation(float x, float y) {
    }

    @Override
    public void setLocation(int x, int y) {
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
        if(parentSize == null) {
            return new Point2<>(margin.c, margin.a);
        }
        int x = (parentSize.x - size.x) / 2;
        int y = (parentSize.y - size.y) / 2;
        return new Point2<>(x, y);
    }
}
