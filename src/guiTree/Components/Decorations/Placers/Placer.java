package guiTree.Components.Decorations.Placers;

import guiTree.Helper.Point2;
import guiTree.Helper.Point4;

public interface Placer {
    void setRelativeLocation(float x, float y);
    void setLocation(int x, int y);
    void setElementSize(int width, int height);
    void setParentSize(int width, int height);
    void setMargins(int up, int down, int left, int right);
    void setMargins(int margin);
    Point4<Integer> getMargins();
    Point2<Integer> getPosition();
}
