package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;

public class TopLeftPlacer extends Placer {

    @Override
    public Point2<Integer> getPosition() {
        return new Point2<>(margin.c, margin.a);
    }
}
