package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Helper.Point2;
import guiTree.Helper.Point4;
import guiTree.Visual;
import guiTree.events.KeyAdapter;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputTextBox extends Visual {
    private List<StringBuilder> lines;
    private Point2<Integer> caretPosition;
    private Point2<Integer> startDragPosition;
    private List<StringBuilder> selectedText;
    private List<Point4<Integer>> selectionRectangles;
    private FontMetrics fontMetrics;
    private int paragraphSpacing;

    public InputTextBox() {
        super();
        lines = new ArrayList<>();
        selectionRectangles = new ArrayList<>();
        lines.add(new StringBuilder());
        caretPosition = new Point2<>(0, 0);
        startDragPosition = new Point2<>(0, 0);
        paragraphSpacing = 1;
        selectedText = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                caretPosition = getCaretPosition(mouseEvent.getX(), mouseEvent.getY());
                setSelection();
                update();
            }
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                caretPosition = getCaretPosition(mouseEvent.getX(), mouseEvent.getY());
                startDragPosition = new Point2<>(caretPosition.x, caretPosition.y);
                setSelection();
                addAnimation(new ColorAnimation(InputTextBox.this, getAccentColor(), getForegroundColor(), 100));
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                setCursor(new Cursor(Cursor.TEXT_CURSOR));
                addAnimation(new ColorAnimation(InputTextBox.this, getBackgroundColor(), getAccentColor(), 100));
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                addAnimation(new ColorAnimation(InputTextBox.this, getAccentColor(), getBackgroundColor(), 100));
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    if(caretPosition.y > 0){
                        if(caretPosition.x > lines.get(caretPosition.y - 1).length()) {
                            caretPosition.x = lines.get(caretPosition.y - 1).length();
                        }
                        caretPosition.y --;
                        if(!keyEvent.isShiftDown()) {
                            startDragPosition = new Point2<>(caretPosition);
                        }
                        setSelection();
                        update();
                        return;
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(caretPosition.y < lines.size() - 1){
                        if(caretPosition.x > lines.get(caretPosition.y + 1).length()) {
                            caretPosition.x = lines.get(caretPosition.y + 1).length();
                        }
                        caretPosition.y ++;
                        if(!keyEvent.isShiftDown()) {
                            startDragPosition = new Point2<>(caretPosition);
                        }
                        setSelection();
                        update();
                        return;
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    if(caretPosition.x == 0){
                        if(caretPosition.y > 0) {
                            caretPosition.y --;
                            caretPosition.x = lines.get(caretPosition.y).length();
                            if(!keyEvent.isShiftDown()) {
                                startDragPosition = new Point2<>(caretPosition);
                            }
                            setSelection();
                            update();
                        }
                        return;
                    }
                    if(keyEvent.isControlDown()) {
                        moveCaretToNextWord(-1);
                    }
                    else {
                        caretPosition.x--;
                    }
                    if(!keyEvent.isShiftDown()) {
                        startDragPosition = new Point2<>(caretPosition);
                    }
                    setSelection();
                    update();
                    return;
                }


                if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if(caretPosition.x == lines.get(caretPosition.y).length()){
                        if(caretPosition.y + 1 < lines.size()) {
                            caretPosition.y++;
                            caretPosition.x = 0;
                            if(!keyEvent.isShiftDown()) {
                                startDragPosition = new Point2<>(caretPosition);
                            }
                            setSelection();
                            update();
                        }
                        return;
                    }
                    if(keyEvent.isControlDown()) {
                        moveCaretToNextWord(1);
                    }
                    else {
                        caretPosition.x++;
                    }
                    if(!keyEvent.isShiftDown()) {
                        startDragPosition = new Point2<>(caretPosition);
                    }
                    setSelection();
                    update();
                    return;
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_C) {
                    if(keyEvent.isControlDown()) {
                        copyToClipboard();
                        return;
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_X) {
                    if(keyEvent.isControlDown()) {
                        cutToClipboard();
                        update();
                        return;
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_V) {
                    if(keyEvent.isControlDown()) {
                        pasteFromClipboard();
                        update();
                        return;
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_A) {
                    if(keyEvent.isControlDown()) {
                        selectAll();
                        update();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent keyEvent) {
                deleteSelection();
                StringBuilder currentLine = lines.get(caretPosition.y);
                if(keyEvent.getKeyChar() == '\n') {
                    caretPosition.y++;
                    lines.add(caretPosition.y, new StringBuilder(currentLine.substring(caretPosition.x)));
                    lines.set(caretPosition.y - 1, new StringBuilder(currentLine.substring(0, caretPosition.x)));
                    caretPosition.x = 0;
                    startDragPosition = new Point2<>(caretPosition);
                    update();
                    return;
                }

                if(keyEvent.getKeyChar() == '\b') {
                    if(caretPosition.x == 0) {
                        if(caretPosition.y == 0) {
                            return;
                        }
                        caretPosition.y --;
                        caretPosition.x = lines.get(caretPosition.y).length();
                        lines.set(caretPosition.y, lines.get(caretPosition.y).append(currentLine));
                        lines.remove(currentLine);
                        update();
                        return;
                    }
                    currentLine.deleteCharAt(caretPosition.x - 1);
                    lines.set(caretPosition.y, currentLine);
                    caretPosition.x --;
                    startDragPosition = new Point2<>(caretPosition);
                    update();
                    return;
                }

                if(keyEvent.isControlDown()){
                    return;
                }

                lines.set(caretPosition.y, lines.get(caretPosition.y).insert(caretPosition.x, (Object)keyEvent.getKeyChar()));
                caretPosition.x ++;
                startDragPosition = new Point2<>(caretPosition);
                update();
            }
        });
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        fontMetrics = null;
    }

    public String getText() {
        StringBuilder builder = new StringBuilder();
        lines.forEach(f -> builder.append(f).append('\n'));
        return builder.substring(0, builder.length() - 1);
    }

    private void copyToClipboard() {
        StringBuilder clipboardText = new StringBuilder();
        for(StringBuilder line: selectedText) {
            clipboardText.append(line).append('\n');
        }
        StringSelection stringSelection = new StringSelection(clipboardText.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    private void cutToClipboard() {
        copyToClipboard();
        deleteSelection();
    }

    private void selectAll() {
        caretPosition.x = lines.get(lines.size() - 1).length();
        caretPosition.y = lines.size() - 1;
        startDragPosition.x = 0;
        startDragPosition.y = 0;
        setSelection();
    }

    private void pasteFromClipboard() {
        Transferable clipboardTransferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
        String clipboardText = null;
        try {
            clipboardText = (String) clipboardTransferable.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        if(clipboardText == null) {
            return;
        }
        clipboardText = clipboardText.substring(0, clipboardText.length() -1 );
        if(clipboardText.indexOf('\n') == -1) {
            lines.get(caretPosition.y).insert(caretPosition.x, clipboardText);
            caretPosition.x += clipboardText.length();
            startDragPosition = caretPosition;
        }
    }

    private Point2<Integer> getPositionOnScreen(int x, int y){
        String currentLine = lines.get(y).toString();
        y *= fontMetrics.getHeight();
        y += paragraphSpacing;
        int width = 0;
        for(int i = 1; i <= currentLine.length(); i++) {
            width += fontMetrics.charWidth(currentLine.charAt(i - 1));
            if(x == i) {
                return new Point2<>(width, y);
            }
        }
        return new Point2<>(0, y);
    }

    private Point2<Integer> getCaretPosition(int x, int y) {
        y -= lines.size() * paragraphSpacing;
        y /= fontMetrics.getHeight();
        if(y > lines.size() - 1) {
            return new Point2<>(lines.get(lines.size() - 1).length(), lines.size() - 1);
        }
        String currentLine = lines.get(y).toString();
        for(int i = 0; i < currentLine.length(); i++) {
            if(x < fontMetrics.charWidth(currentLine.charAt(i)) / 2) {
                return new Point2<>(i, y);
            }
            x -= fontMetrics.charWidth(currentLine.charAt(i));
        }
        return new Point2<>(currentLine.length(), y);
    }

    private void moveCaretToNextWord(int direction) {
        if(direction > 0) {
            if (lines.get(caretPosition.y).length() == caretPosition.x) {
                if (lines.size() > caretPosition.y + 1) {
                    caretPosition.y++;
                    caretPosition.x = 0;
                    return;
                }
            }
        }
        else {
            if (caretPosition.x == 0) {
                if (caretPosition.y > 0) {
                    caretPosition.y--;
                    caretPosition.x = lines.get(caretPosition.y).length();
                    return;
                }
            }
        }
        StringBuilder currentLine = lines.get(caretPosition.y);
        char currentChar;
        currentChar = currentLine.charAt(caretPosition.x + direction);
        while(currentChar == ' ' ){
            caretPosition.x += direction;
            currentChar = currentLine.charAt(caretPosition.x);
            if((currentLine.length() == caretPosition.x && direction > 0) || (caretPosition.x == 0 && direction < 0)) {
                return;
            }
        }
        do {
            caretPosition.x += direction;
            if(currentLine.length() == caretPosition.x && direction > 0) {
                return;
            }
            currentChar = currentLine.charAt(caretPosition.x);
             if(caretPosition.x == 0 && direction < 0) {
                return;
            }
        } while((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z'));
    }

    private void deleteSelection() {
        selectionRectangles.clear();
        selectedText.clear();

        if(caretPosition.equals(startDragPosition)) {
            return;
        }
        Point2<Integer> selectionStart;
        Point2<Integer> selectionEnd;
        if(caretPosition.compareTo(startDragPosition) < 0) {
            selectionStart = new Point2<>(caretPosition);
            selectionEnd = new Point2<>(startDragPosition);
        }
        else {
            selectionStart = new Point2<>(startDragPosition);
            selectionEnd = new Point2<>(caretPosition);
        }
        caretPosition = selectionStart;

        if(selectionStart.y.equals(selectionEnd.y)) {
            StringBuilder currentLine = lines.get(selectionStart.y);
            int lineLength = currentLine.length();
            int newLength = lineLength - selectionEnd.x + selectionStart.x;
            currentLine.insert(selectionStart.x, currentLine.substring(selectionEnd.x));
            currentLine = new StringBuilder(currentLine.substring(0, newLength));
            lines.set(selectionStart.y, currentLine);
            return;
        }

        while(selectionStart.y + 1 < selectionEnd.y) {
            lines.remove(selectionStart.y + 1);
            selectionEnd.y--;
        }

        StringBuilder currentLine = lines.get(selectionStart.y);
        currentLine.insert(selectionStart.x, lines.get(selectionEnd.y).substring(selectionEnd.x));
        currentLine = new StringBuilder(currentLine.substring(0, selectionStart.x + lines.get(selectionEnd.y).substring(selectionEnd.x).length()));
        lines.remove((int)selectionEnd.y);
        lines.set(selectionStart.y, currentLine);
    }

    private void setSelection() {
        selectedText.clear();
        selectionRectangles.clear();
        if(caretPosition.equals(startDragPosition)) {
            return;
        }
        Point2<Integer> selectionStart;
        Point2<Integer> selectionEnd;
        if(caretPosition.compareTo(startDragPosition) < 0) {
            selectionStart = new Point2<>(caretPosition);
            selectionEnd = new Point2<>(startDragPosition);
        }
        else {
            selectionStart = new Point2<>(startDragPosition);
            selectionEnd = new Point2<>(caretPosition);
        }

        if(selectionEnd.y.equals(selectionStart.y)) {
            selectedText.add(new StringBuilder());
            Point2<Integer> startRect = getPositionOnScreen(selectionStart.x, selectionStart.y);
            Point2<Integer> endRect = getPositionOnScreen(selectionEnd.x, selectionEnd.y);
            selectionRectangles.add(new Point4<>(startRect.x, startRect.y + 3, endRect.x, endRect.y + fontMetrics.getHeight() + 3));
            StringBuilder currentLine = lines.get(selectionStart.y);
            for(int x = selectionStart.x; x < selectionEnd.x; x++) {
                selectedText.get(0).insert(selectedText.get(0).length(), currentLine.charAt(x));
            }
            return;
        }

        for(int y = selectionStart.y; y < selectionEnd.y; y++) {
            StringBuilder currentLine = lines.get(y);
            Point2<Integer> startRect = getPositionOnScreen(selectionStart.x, y);
            Point2<Integer> endRect = getPositionOnScreen(currentLine.length(), y);
            selectionRectangles.add(new Point4<>(startRect.x, startRect.y + 3, endRect.x, endRect.y + fontMetrics.getHeight() + 3));
            selectedText.add(new StringBuilder());
            int selectionIndex = 0;
            for(int x = selectionStart.x; x < currentLine.length(); x++) {
                selectedText.get(selectedText.size() - 1).insert(selectionIndex++, currentLine.charAt(x));
            }
            selectionStart.x = 0;
        }
        StringBuilder currentLine = lines.get(selectionEnd.y);
        selectedText.add(new StringBuilder());
        Point2<Integer> startRect = getPositionOnScreen(selectionStart.x, selectionEnd.y);
        Point2<Integer> endRect = getPositionOnScreen(selectionEnd.x, selectionEnd.y);
        selectionRectangles.add(new Point4<>(startRect.x, startRect.y + 3, endRect.x, endRect.y + fontMetrics.getHeight() + 3));
        int selectionIndex = 0;
        for(int x = 0; x < selectionEnd.x; x++) {
            selectedText.get(selectedText.size() - 1).insert(selectionIndex++, currentLine.charAt(x));
        }
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();

        g.setColor(getPaintColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        if(fontMetrics == null) {
            fontMetrics = g.getFontMetrics();
        }

        g.setColor(Color.BLUE);
        for(Point4<Integer> rect: selectionRectangles) {
            g.fillRect(rect.a, rect.b, rect.c - rect.a, rect.d - rect.b);
        }

        int y = fontMetrics.getHeight();
        g.setColor(getFontColor());
        for(StringBuilder line: lines) {
            if(caretPosition.y == lines.indexOf(line) && isFocused()) {
                int x = 0;
                for(int i = 0; i < caretPosition.x; i++) {
                    x += fontMetrics.charWidth(line.charAt(i));
                }
                g.setColor(Color.BLACK);
                g.drawLine(x, y - fontMetrics.getHeight() + 3, x, y + 3);
            }
            g.setColor(getFontColor());
            g.drawString(line.toString(), 0, y);
            y += fontMetrics.getHeight();
        }
        g.dispose();
    }
}
