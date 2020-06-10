package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;

public class BottomRightPlacer extends Placer {

    @Override
    public Point2<Integer> getPosition() {
        if(parentSize == null) {
            return new Point2<>(margin.c, margin.a);
        }
        int y = parentSize.y - size.y - margin.b;
        int x = parentSize.x - size.x - margin.d;
        return new Point2<>(x, y);
    }
}
