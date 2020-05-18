package guiTree.Components;

import guiTree.Animations.ColorAnimation;
import guiTree.Helper.Point2;
import guiTree.Visual;
import guiTree.events.KeyAdapter;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class InputTextBox extends Visual {
    private boolean visible;
    private List<StringBuilder> lines;
    private String title;
    private Point2<Integer> caretPosition;
    private FontMetrics fontMetrics;
    private int paragraphSpacing;

    public InputTextBox() {
        this(true, "");
    }

    public InputTextBox(Boolean visible) {
        this(visible, "");
    }

    public InputTextBox(String title) {
        this(true, title);
    }

    public InputTextBox(Boolean visible, String title) {
        super();
        this.title = title;
        this.visible = visible;
        lines = new ArrayList<>();
        lines.add(new StringBuilder());
        caretPosition = new Point2<>(0, 0);
        paragraphSpacing = 1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                caretPosition = getCaretPosition(mouseEvent.getX(), mouseEvent.getY());
                System.out.println("Caret Position: " + caretPosition);
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
                        update();
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(caretPosition.y < lines.size() - 1){
                        if(caretPosition.x > lines.get(caretPosition.y + 1).length()) {
                            caretPosition.x = lines.get(caretPosition.y + 1).length();
                        }
                        caretPosition.y ++;
                        update();
                    }
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    if(caretPosition.x == 0){
                        if(caretPosition.y > 0) {
                            caretPosition.y --;
                            caretPosition.x = lines.get(caretPosition.y).length();
                            update();
                        }
                        return;
                    }
                    caretPosition.x --;
                    update();
                    return;
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if(caretPosition.x == lines.get(caretPosition.y).length()){
                        if(caretPosition.y + 1 < lines.size()) {
                            caretPosition.y++;
                            caretPosition.x = 0;
                            update();
                        }
                        return;
                    }
                    caretPosition.x ++;
                    update();
                }
            }

            @Override
            public void keyTyped(KeyEvent keyEvent) {
                StringBuilder currentLine = lines.get(caretPosition.y);
                if(keyEvent.getKeyChar() == '\n') {
                    caretPosition.y++;
                    lines.add(caretPosition.y, new StringBuilder(currentLine.substring(caretPosition.x)));
                    lines.set(caretPosition.y - 1, new StringBuilder(currentLine.substring(0, caretPosition.x)));
                    caretPosition.x = 0;
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
                    update();
                    return;
                }

                lines.set(caretPosition.y, lines.get(caretPosition.y).insert(caretPosition.x, (Object)keyEvent.getKeyChar()));
                caretPosition.x ++;
                update();
            }
        });
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        fontMetrics = null;
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

    @Override
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();

        System.out.println("Caret: " + caretPosition);
        g.setColor(getPaintColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        if(fontMetrics == null) {
            fontMetrics = g.getFontMetrics();
            return;
        }

        int y = fontMetrics.getHeight();
        g.setColor(getFontColor());
        for(StringBuilder line: lines) {
            if(caretPosition.y == lines.indexOf(line) && isFocused()) {
                int x = 0;
                for(int i = 0; i < caretPosition.x; i++) {
                    x += fontMetrics.charWidth(line.charAt(i));
                }
                g.drawLine(x, y - fontMetrics.getHeight() + 3, x, y + 3);
            }
//            g.drawString(line.toString(), 0, fontMetrics.getHeight() * (lines.indexOf(line) + 1));
            g.drawString(line.toString(), 0, y);
            y += fontMetrics.getHeight();
        }
    }
}
