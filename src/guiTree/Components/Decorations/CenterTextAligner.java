package guiTree.Components.Decorations;

import guiTree.Helper.Point2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CenterTextAligner implements TextAligner{
    private int width;
    private int height;
    private FontMetrics fontMetrics;
    private int spacing;
    private int textHeight;
    private List<String> wholeText;

    @Override
    public Point2<Integer> alignLine(int line) {
        int x = (width - fontMetrics.stringWidth(wholeText.get(line))) / 2;
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
        x -= (width - fontMetrics.stringWidth(currentLine)) / 2;
        for(int i = 0; i < currentLine.length(); i++) {
            if(x < (fontMetrics.charWidth(currentLine.charAt(i))) / 2) {
                return new Point2<>(i, y);
            }
            x -= fontMetrics.charWidth(currentLine.charAt(i));
        }
        return new Point2<>(0, y);
    }

    @Override
    public Point2<Integer> getPositionOnScreen(int x, int y) {
        String currentLine = wholeText.get(y);
        y = (fontMetrics.getHeight() + spacing) * y;

        int width = (this.width - fontMetrics.stringWidth(currentLine)) / 2;
        width += fontMetrics.stringWidth(currentLine.substring(0, x));
        return new Point2<>(width, y);
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
        this.textHeight = fontMetrics.getHeight();
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
