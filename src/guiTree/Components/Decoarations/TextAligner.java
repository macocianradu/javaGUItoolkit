package guiTree.Components.Decoarations;

import guiTree.Helper.Point2;

import java.awt.*;
import java.util.List;

public interface TextAligner {
    Point2<Integer> alignLine(int line);
    Point2<Integer> getCaretPosition(int x, int y);
    Point2<Integer> getPositionOnScreen(int x, int y);
    void setWholeText(List<StringBuilder> wholeText);
    void setSpacing(int spacing);
    void setFontMetrics(FontMetrics fontMetrics);
    void setSize(int width, int height);
}
