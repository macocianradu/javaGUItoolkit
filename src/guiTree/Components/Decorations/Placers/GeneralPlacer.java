package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;
import guiTree.Helper.Point4;

public class GeneralPlacer extends Placer {

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
