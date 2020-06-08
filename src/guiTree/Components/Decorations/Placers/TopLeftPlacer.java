package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;
import guiTree.Helper.Point4;

public class TopLeftPlacer implements Placer {
    private Point4<Integer> margin;

    public TopLeftPlacer() {
        margin = new Point4<>(0, 0, 0, 0);
    }

    @Override
    public void setRelativeLocation(float relativeX, float relativeY) {
    }

    @Override
    public void setLocation(int x, int y) {

    }

    @Override
    public void setElementSize(int width, int height) {
    }

    @Override
    public void setParentSize(int width, int height) {
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
        return new Point2<>(margin.c, margin.a);
    }
}
