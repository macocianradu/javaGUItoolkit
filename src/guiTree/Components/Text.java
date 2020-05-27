package guiTree.Components;

import guiTree.Components.Decoarations.CenterTextAligner;
import guiTree.Components.Decoarations.LeftTextAligner;
import guiTree.Components.Decoarations.RightTextAligner;
import guiTree.Components.Decoarations.TextAligner;
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

public class Text extends Visual {
    private List<StringBuilder> lines;
    private String title;
    private Point2<Integer> caretPosition;
    private Point2<Integer> startDragPosition;
    private List<StringBuilder> selectedText;
    private List<Point4<Integer>> selectionRectangles;
    private FontMetrics fontMetrics;
    private TextAligner textAligner;
    private int paragraphSpacing;
    private boolean selectable;

    public Text() {
        this(true, "");
    }

    public Text(Boolean visible) {
        this(visible, "");
    }

    public Text(String title) {
        this(true, title);
    }

    public Text(Boolean visible, String title) {
        super();
        this.title = title;
        lines = new ArrayList<>();
        selectionRectangles = new ArrayList<>();
        lines.add(new StringBuilder());
        caretPosition = new Point2<>(0, 0);
        startDragPosition = new Point2<>(0, 0);
        textAligner = new LeftTextAligner();
        paragraphSpacing = 1;
        selectable = true;
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
            }
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if(selectable) {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_C) {
                    if(keyEvent.isControlDown()) {
                        copyToClipboard();
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
        });
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        fontMetrics = null;
    }

    @Override
    public void setSize() {
        super.setSize();
        textAligner.setSize(getWidth(), getHeight());
    }

    public void setSpacing(int spacing) {
        this.paragraphSpacing = spacing;
        textAligner.setSpacing(spacing);
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setText(String text) {
        lines.clear();
        int lineIndex = text.indexOf('\n');
        while(lineIndex != -1) {
            lines.add(new StringBuilder(text.substring(0, lineIndex)));
            text = text.substring(lineIndex + 1);
            lineIndex = text.indexOf('\n');
        }
        lines.add(new StringBuilder(text));
        textAligner.setWholeText(lines);
        update();
    }

    public void setAlignment(String textAlignment) {
        textAlignment = textAlignment.toLowerCase();
        switch(textAlignment) {
            case "left": {
                textAligner = new LeftTextAligner();
                break;
            }
            case "right": {
                textAligner = new RightTextAligner();
                break;
            }
            case "center": {
                textAligner = new CenterTextAligner();
                break;
            }
            default: {
                System.err.println("Alignment does not exist");
                return;
            }
        }
        textAligner.setWholeText(lines);
        textAligner.setSize(getWidth(), getHeight());
        if(fontMetrics != null) {
            textAligner.setFontMetrics(fontMetrics);
        }
        textAligner.setSpacing(paragraphSpacing);
    }

    private void copyToClipboard() {
        StringBuilder clipboardText = new StringBuilder();
        for(StringBuilder line: selectedText) {
            clipboardText.append(line).append('\n');
        }
        StringSelection stringSelection = new StringSelection(clipboardText.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
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
        return textAligner.getPositionOnScreen(x, y);
    }

    private Point2<Integer> getCaretPosition(int x, int y) {
        return textAligner.getCaretPosition(x, y);
    }

    private void setSelection() {
        if(!selectable) {
            return;
        }
        selectedText.clear();
        selectionRectangles.clear();
        System.out.println(caretPosition + " " + startDragPosition);
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
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();
        if(fontMetrics == null) {
            fontMetrics = g.getFontMetrics();
            textAligner.setFontMetrics(fontMetrics);
        }

        if(selectable) {
            g.setColor(Color.BLUE);
            for (Point4<Integer> rect : selectionRectangles) {
                g.fillRect(rect.a, rect.b, rect.c - rect.a, rect.d - rect.b + paragraphSpacing);
            }
        }

        g.setColor(getFontColor());
        for(StringBuilder line: lines) {
            Point2<Integer> position = textAligner.alignLine(lines.indexOf(line));
            g.drawString(line.toString(), position.x, position.y);
        }
    }
}
