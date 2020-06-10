package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;

public class MiddleLeftPlacer extends Placer {

    @Override
    public Point2<Integer> getPosition() {
        if(parentSize == null) {
            return new Point2<>(margin.c, margin.a);
        }
        int y = (parentSize.y - size.y) / 2;
        return new Point2<>(margin.c, y);
    }
}
