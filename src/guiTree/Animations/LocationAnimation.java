package guiTree.Animations;

import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Visual;

public class LocationAnimation implements AnimationInterface {
    private Point2<Float> from;
    private Point2<Integer> to;
    private Point2<Float> offset;
    private Visual element;

    public LocationAnimation(Visual v, Point2<Integer> from, Point2<Integer> to, int ms) {
        this.from = new Point2<>(from.x.floatValue(), from.y.floatValue());
        this.to = to;
        this.offset = new Point2<>((float)(to.x - from.x) * 1000 / FPS / ms, (float)(to.y - from.y) * 1000 / FPS / ms);
        this.element = v;
        Debugger.log("Created animation for " + v.getName() + " from x: " + from.x + " y: " + from.y + " to x: " + to.x + " y: " + to.y, Debugger.Tag.ANIMATIONS);
    }

    @Override
    public boolean step() {
        if(from.x > to.x - 1 && from.x < to.x + 1 && from.y > to.y - 1 && from.y < to.y + 1) {
            element.setLocation(to.x, to.y);
            Debugger.log("Animation for " + element.getName() + " finished", Debugger.Tag.ANIMATIONS);
            return true;
        }
        from.x += offset.x;
        from.y += offset.y;
        element.setLocation(Math.round(from.x), Math.round(from.y));
        element.update();
        return false;
    }
}
