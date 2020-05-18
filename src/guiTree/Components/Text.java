package guiTree.Components;

import guiTree.Visual;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Text extends Visual {
    private String text;
    private boolean selectable;
    private int startIndex;
    private int endIndex;
    private int textWidth;
    private int textHeight;
    private boolean inside;
    private int[] characterWidthMap;
    private FontMetrics fontMetrics;
    private int textX;
    private int textY;

    public Text() {
        this(0, 0, "");
    }

    public Text(int x, int y, String text) {
        super(x, y);
        this.text = text;
        inside = false;
        characterWidthMap = new int[text.length()];
        startIndex = -1;
        endIndex = -1;
        textX = 0;
        textY = 0;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                if(isOverText(x, y)) {
                    startIndex = getCharAt(x);
                    System.out.println("Start Index: " + startIndex);
                    return;
                }
                startIndex = -1;
                endIndex = -1;
                update();
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();

                if(startIndex != -1) {
                    endIndex = getCharAt(x);
                    System.out.println("End index: " + endIndex);
                }
                update();
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(inside) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                if(isOverText(x, y)) {
                    if(!inside) {
                        setCursor(new Cursor(Cursor.TEXT_CURSOR));
                        inside = true;
                    }
                    return;
                }
                if(inside) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    inside = false;
                }
            }
        });
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        characterWidthMap = new int[text.length()];
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    private int getCharAt(int x) {
        int location = (getWidth() - textWidth)/2;
        for(int i = 0; i < text.length(); i++) {
            if(x < location + characterWidthMap[i] / 2) {
                return i;
            }
            location += characterWidthMap[i];
        }
        return text.length()-1;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        fontMetrics = null;
    }

    private boolean isOverText(int x, int y) {
        if(x < textX || x > textX + textWidth) {
            return false;
        }
        if(y > textY || y < textY - textHeight) {
            return false;
        }
        System.out.println("Text X: " + textX + " Text Y: " + textY + " x: " + x + " y: " + y + " textWidth: " + textWidth + " textHeight: " + textHeight);
        return true;
    }

    private void setCharacterWidthMap() {
        for(int i = 0; i < text.length(); i++) {
            int charWidth = fontMetrics.charWidth(text.charAt(i));
            characterWidthMap[i] = charWidth;
        }
        textWidth = fontMetrics.stringWidth(text);
        textHeight = fontMetrics.getHeight();
        textX = (getWidth() - textWidth)/2;
        textY = (getHeight() + textHeight)/2;
    }


    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();

        if(getFont() != null) {
            g.setFont(getFont());
        }
        if(fontMetrics == null) {
            fontMetrics = g.getFontMetrics();
            setCharacterWidthMap();
        }

        if(startIndex != -1 && endIndex != -1) {
            int startX;
            int endX;
            if(startIndex > endIndex) {
                endX = startIndex - 1;
                startX = endIndex;
            }
            else {
                startX = startIndex;
                endX = endIndex;
            }
            int highlightStartX = textX;
            int highlightEndX = textX;

            for(int i = 0; i <= endX; i++) {
                if(i < startX) {
                    highlightStartX += characterWidthMap[i];
                }
                highlightEndX += characterWidthMap[i];
            }

            g.setColor(Color.BLUE);
            g.fillRect(highlightStartX, textY - textHeight + 3, highlightEndX - highlightStartX, textHeight);
        }

        g.setColor(getFontColor());

        g.drawString(text, textX, textY);

        g.dispose();
    }
}
