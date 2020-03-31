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
        if(from.a > to.a - 1 && from.a < to.a + 1 &&
                from.b > to.b - 1 && from.b < to.b + 1 &&
                from.c > to.c - 1 && from.c < to.c + 1 &&
                from.d > to.d - 1 && from.d < to.d + 1) {
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
