package guiTree.Components.Decoarations;

import guiTree.Helper.Point2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RightTextAligner implements TextAligner{
    private int spacing;
    private FontMetrics fontMetrics;
    private List<String> wholeText;
    private int textHeight;
    private int width;

    @Override
    public Point2<Integer> alignLine(String text, int line) {
        int x = width - fontMetrics.stringWidth(text);
        int y = (line + 1) * textHeight;
        y += spacing * line;
        return new Point2<>(x, y);
    }

    @Override
    public Point2<Integer> getCaretPosition(int x, int y) {
        y -= wholeText.size() * spacing;
        y /= fontMetrics.getHeight();
        if(y > wholeText.size() - 1) {
            return new Point2<>(wholeText.get(wholeText.size() - 1).length(), wholeText.size() - 1);
        }
        if(y < 0) {
            return new Point2<>(0, 0);
        }
        String currentLine = wholeText.get(y);
        int index = currentLine.length();
        for(int i = width - currentLine.length(); i < width; i++) {
            if(x > width - fontMetrics.charWidth(currentLine.charAt(index - 1))) {
                return new Point2<>(index, y);
            }
            x += fontMetrics.charWidth(currentLine.charAt(index - 1));
            index--;
        }
        return new Point2<>(0, y);
    }

    @Override
    public Point2<Integer> getPositionOnScreen(int x, int y) {
        String currentLine = wholeText.get(y);
        y = (fontMetrics.getHeight() + spacing) * y;
        int width = this.width;
        for(int i = currentLine.length(); i > 0; i--) {
            if(x == i) {
                return new Point2<>(width, y);
            }
            width -= fontMetrics.charWidth(currentLine.charAt(i - 1));
        }
        return new Point2<>(this.width - fontMetrics.stringWidth(currentLine), y);
    }

    @Override
    public void setWholeText(List<StringBuilder> wholeText) {
        this.wholeText = new ArrayList<>();
        for(StringBuilder line: wholeText) {
            this.wholeText.add(line.toString());
        }
    }

    @Override
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void setFontMetrics(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
        textHeight = fontMetrics.getHeight();
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
    }
}
