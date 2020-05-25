package guiTree.Animations;

import guiTree.Helper.*;
import guiTree.Visual;

import java.awt.*;

public class ColorAnimation implements AnimationInterface {
    private Point4<Float> from;
    private Point4<Integer> to;
    private Point4<Float> offset;
    private Visual element;

    public ColorAnimation(Visual v, Color from, Color to, int ms) {
        this.from = new Point4<>((float)from.getRed(), (float)from.getGreen(), (float)from.getBlue(), (float)from.getAlpha());
        this.to = new Point4<>(to.getRed(), to.getGreen(), to.getBlue(), to.getAlpha());
        this.offset = new Point4<>((this.to.a - this.from.a) * 1000 / FPS / ms,
                (this.to.b - this.from.b) * 1000 / FPS / ms,
                (this.to.c - this.from.c) * 1000 / FPS / ms,
                (this.to.d - this.from.d) * 1000 / FPS / ms);
        this.element = v;
        Debugger.log("Created color animation for " + v.getName() +
                " from r: " + this.from.a +
                " g: " + this.from.b +
                " b: " + this.from.c +
                " a: " + this.from.d +
                " to r: " + this.to.a +
                " g: " + this.to.b +
                " b: " + this.to.c +
                " a: " + this.to.d, Debugger.Tag.ANIMATIONS);
    }

    @Override
    public boolean step() {
        if(((from.a + offset.a >= to.a && offset.a >= 0) || (from.a + offset.a <= to.a && offset.a <= 0)) &&
                ((from.b + offset.b >= to.b && offset.b >= 0) || (from.b + offset.b <= to.b && offset.b <= 0)) &&
                ((from.c + offset.c >= to.c && offset.c >= 0) || (from.c + offset.c <= to.c && offset.c <= 0)) &&
                ((from.d + offset.d >= to.d && offset.d >= 0) || (from.d + offset.d <= to.d && offset.d <= 0))) {
            element.setPaintColor(new Color(to.a, to.b, to.c, to.d));
            Debugger.log("Animation for " + element.getName() + " finished", Debugger.Tag.ANIMATIONS);
            return true;
        }
        from.a += offset.a;
        from.b += offset.b;
        from.c += offset.c;
        from.d += offset.d;
        element.setPaintColor(new Color(Math.round(from.a), Math.round(from.b), Math.round(from.c), Math.round(from.d)));
        element.update();
        return false;
    }
}
