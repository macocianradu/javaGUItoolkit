package guiTree.Animations;

import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Visual;

public class SizeAnimation implements AnimationInterface {
    private Point2<Float> from;
    private Point2<Integer> to;
    private Point2<Float> offset;
    private Visual element;

    public SizeAnimation(Visual v, Point2<Integer> from, Point2<Integer> to, int ms) {
        this.from = new Point2<>(from.x.floatValue(), from.y.floatValue());
        this.to = to;
        this.offset = new Point2<>((float)(to.x - from.x) * 1000 / FPS / ms, (float)(to.y - from.y) * 1000 / FPS / ms);
        this.element = v;
        Debugger.log("Created size animation for " + v.getName() + " from width: " + from.x + " height: " + from.y + " to width: " + to.x + " height: " + to.y, Debugger.Tag.ANIMATIONS);
    }

    @Override
    public boolean step() {
        if(((from.x + offset.x >= to.x && offset.x >= 0) || (from.x + offset.x <= to.x && offset.x <= 0)) &&
                ((from.y + offset.y >= to.y && offset.y >= 0) || (from.y + offset.y <= to.y && offset.y <= 0))) {
            element.setSize(to.x, to.y);
            Debugger.log("Animation for " + element.getName() + " finished", Debugger.Tag.ANIMATIONS);
            return true;
        }
        from.x += offset.x;
        from.y += offset.y;
        element.setSize(Math.round(from.x), Math.round(from.y));
        element.update();
        return false;
    }
}
