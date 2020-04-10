package guiTree;

import guiTree.Animations.AnimationInterface;
import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Helper.Timer;
import guiTree.events.KeyListener;
import guiTree.events.MouseListener;
import guiTree.events.MouseWheelListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Visual {
    /*--------------------------------------------------------------------
                            Constant Values
    ---------------------------------------------------------------------*/

    public static final int SIZE_CHANGED = 1;
    public static final int LOCATION_CHANGED = 2;
    private static List<AnimationInterface> animations = new ArrayList<>();

    /*--------------------------------------------------------------------
                            Tree Elements
    ---------------------------------------------------------------------*/

    private List<Visual> children;
    private Visual parent;
    private BufferedImage imageBuffer;
    private String name;
    private List<MouseListener> mouseListeners;
    private List<MouseWheelListener> mouseWheelListeners;
    private List<KeyListener> keyListeners;

    /*--------------------------------------------------------------------
                        Attributes
    ---------------------------------------------------------------------*/

    private Integer width;
    private Integer height;
    private Float relativeWidth;
    private Float relativeHeight;
    private Integer locationX;
    private Integer locationY;
    private Integer absoluteX;
    private Integer absoluteY;
    private Float relativeX;
    private Float relativeY;
    private Boolean hasBorder;
    private Font font;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color accentColor;
    private Color fontColor;
    private Color borderColor;
    private Color paintColor;
    private Boolean active;
    private Boolean dirty;
    private static Visual entered;
    private static Visual focused;
    private Boolean pressed;

    /*--------------------------------------------------------------------
                        Constructors
    ---------------------------------------------------------------------*/

    public Visual() {
        this(1, 1);
    }

    public Visual(int width, int height) {
        this.children = new ArrayList<>();
        this.mouseWheelListeners = new ArrayList<>();
        this.mouseListeners = new ArrayList<>();
        this.keyListeners = new ArrayList<>();
        this.parent = null;
        this.name = "";
        this.backgroundColor = Color.WHITE;
        this.foregroundColor = Color.BLUE;
        this.fontColor = Color.BLACK;
        this.borderColor = Color.BLACK;
        this.accentColor = Color.BLUE;

        this.dirty = true;
        this.hasBorder = false;
        this.active = this instanceof Window;
        this.pressed = false;

        this.width = width;
        this.height = height;
        this.relativeWidth = -1.0f;
        this.relativeHeight = -1.0f;
        this.relativeX = -1.0f;
        this.relativeY = -1.0f;

        this.locationX = 0;
        this.locationY = 0;
        this.absoluteX = 0;
        this.absoluteY = 0;
    }

    /*--------------------------------------------------------------------
                    Attributes Setters
    ---------------------------------------------------------------------*/

    public void setName(String name) {
        this.name = name;
    }

    public void setSize() {
        if(parent != null) {
            if(relativeWidth > 0.0) {
                width = Math.round(relativeWidth * parent.width);
            }
            if(relativeHeight > 0.0) {
                height = Math.round(relativeHeight * parent.height);
            }

        }
        initializeImageBuffer();

        for(Visual v: children) {
            if(v.relativeHeight > 0.0 || v.relativeWidth > 0.0) {
                v.setSize();
            }
            if(v.relativeX >= 0.0 || v.relativeY >= 0.0) {
                v.setLocation();
            }
        }
        propagateDirt();
        notifyParent(this, SIZE_CHANGED);
    }

    public void setHeight(Integer height) {
        setSize(getWidth(), height);
    }

    public void setWidth(Integer width) {
        setSize(width, getHeight());
    }

    public void setHeight(Float height) {
        setSize(relativeWidth, height);
    }

    public void setWidth(Float width) {
        setSize(width, relativeHeight);
    }

    public void setSize(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        setSize();
    }

    public void setSize(Float width, Float height) {
        relativeWidth = width;
        relativeHeight = height;
        setSize();
    }

    public void setLocation() {
        if(parent != null) {
            if(relativeX >= 0.0) {
                locationX = Math.round(relativeX * parent.width);
            }
            if(relativeY >= 0.0) {
                locationY = Math.round(relativeY * parent.height);
            }
        }

        calculateAbsoluteLocation();
        propagateDirt();
        notifyParent(this, LOCATION_CHANGED);
    }

    public void setLocation(Float x, Float y) {
        relativeX = x;
        relativeY = y;
        setLocation();
    }

    public void setLocation(Integer x, Integer y) {
        this.locationX = x;
        this.locationY = y;
        setLocation();
    }

    public void setLocationX(Integer x) {
        setLocation(x, getLocationY());
    }

    public void setLocationY(Integer y) {
        setLocation(getLocationX(), y);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.paintColor = backgroundColor;
        propagateDirt();
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        propagateDirt();
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        propagateDirt();
    }

    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
        propagateDirt();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        propagateDirt();
    }

    public void setPaintColor(Color paintColor) {
        this.paintColor = paintColor;
        propagateDirt();
    }

    public void setHasBorder(Boolean hasBorder) {
        this.hasBorder = hasBorder;
        propagateDirt();
    }

    /*--------------------------------------------------------------------
                    Attributes Getters
    ---------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLocationX() {
        return this.locationX;
    }

    public int getLocationY() {
        return this.locationY;
    }

    public Point2<Integer> getLocation() {
        return new Point2<>(locationX, locationY);
    }

    public Point2<Float> getRelativeLocation() {
        return new Point2<>(relativeX, relativeY);
    }

    public Font getFont() {
        return font;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public Color getAccentColor() {
        return accentColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getPaintColor() {
        return paintColor;
    }

    public Boolean getHasBorder() {
        return hasBorder;
    }

    /*--------------------------------------------------------------------
                            Tree Methods
    ---------------------------------------------------------------------*/

    public Visual findByName(String name) {
        if(this.name.equals(name)){
            return this;
        }
        else{
            for(Visual child: children){
                Visual visual;
                visual = child.findByName(name);
                if(visual != null){
                    return visual;
                }
            }
        }
        return null;
    }

    public void addVisual(Visual child) {
        this.children.add(child);
        child.setParent(this);
        child.setLocation();
        child.setSize();

        if(this.active) {
            child.activate();
        }
        propagateDirt();
    }

    public void removeVisual(Visual child) {
        if(child == null) {
            return;
        }
        this.children.remove(child);
        child.setParent(null);
        child.imageBuffer = null;
        child.deactivate();
        propagateDirt();
    }

    private void setParent(Visual parent) {
        this.parent = parent;
    }

    public void handleNotification(Visual v, int notify) {

    }

    public void handleNotification(int notify) {

    }

    public void notifyParent(Visual v, int notify) {
        if(parent != null) {
            if(v == null) {
                parent.handleNotification(notify);
            }
            else {
                parent.handleNotification(v, notify);
            }
        }
    }

    public void notifyParent(int notify) {
        notifyParent(null, notify);
    }

    public void addAnimation(AnimationInterface animation) {
        animations.add(animation);
    }

    public void removeAnimation(AnimationInterface animation) {
        animations.remove(animation);
    }

    public void repaint() {
        Debugger.log("Called repaint from " + name, Debugger.Tag.PAINTING);
        for(int i = 0; i < animations.size(); i++) {
            if(animations.get(i).step()) {
                animations.remove(animations.get(i));
                i--;
            }
        }
        if(dirty && active) {
            revalidate();
        }
    }

    private void revalidate() {
        Timer timer = new Timer();
        Debugger.log("Revalidating " + name, Debugger.Tag.PAINTING);
        timer.startTiming();

        clearImageBuffer();
        this.paint(imageBuffer);
        for (Visual v : children) {
            if (v.dirty && v.active) {
                v.revalidate();
            }
            imageBuffer.getGraphics().drawImage(v.imageBuffer, v.locationX, v.locationY, null);
        }
        dirty = false;
        if(!(this instanceof Window)){
            long time = timer.stopTiming();
            Debugger.log("Finished Revalidating " + name + ": " + time, Debugger.Tag.PAINTING);
            return;
        }
        Window window = (Window)this;
        window.setFrameImageBuffer(imageBuffer);

        long time = timer.stopTiming();
        Debugger.log("Finished Revalidating " + name + ": " + time, Debugger.Tag.PAINTING);
        window.revalidate();
    }

    public void update() {
        propagateDirt();
    }

    public void paint(BufferedImage imageBuffer) {
    }

    /*--------------------------------------------------------------------
                            Listener Methods
    ---------------------------------------------------------------------*/

    public void addMouseListener(MouseListener mouseListener) {
        this.mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(MouseListener mouseListener) {
        this.mouseListeners.remove(mouseListener);
    }

    public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        this.mouseWheelListeners.add(mouseWheelListener);
    }

    public void removeMouseWheelListener(MouseWheelListener mouseWheelListener) {
        this.mouseWheelListeners.remove(mouseWheelListener);
    }

    public void addKeyListener(KeyListener keyListener) {
        this.keyListeners.add(keyListener);
    }

    public void removeKeyListener(KeyListener keyListener) {
        this.keyListeners.remove(keyListener);
    }

    void mouseClicked(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseClicked(mouseEvent);
        }
        Debugger.log("Clicked " + entered.name, Debugger.Tag.LISTENER);
    }

    void mouseReleased(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseReleased(mouseEvent);
        }
        Debugger.log("Released " + entered.name, Debugger.Tag.LISTENER);
        entered.pressed = false;
    }

    void mousePressed(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mousePressed(mouseEvent);
        }
        entered.pressed = true;
        focused = entered;
        Debugger.log("Pressed " + entered.name, Debugger.Tag.LISTENER);
    }

    void mouseEntered(MouseEvent mouseEvent) {
        if(entered != null && entered.pressed){
            return;
        }
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()){
                v.mouseEntered(mouseEvent);
                return;
            }
        }
        entered = this;
        for(MouseListener mouseListener: mouseListeners) {
            mouseListener.mouseEntered(mouseEvent);
        }
        Debugger.log("Entered " + entered.name, Debugger.Tag.LISTENER);
    }

    void mouseExited(MouseEvent mouseEvent) {
        if(entered == null) {
            return;
        }
        if(entered.pressed) {
            return;
        }
        for (MouseListener mouseListener : entered.mouseListeners) {
            mouseListener.mouseExited(mouseEvent);
        }
        Debugger.log("Exited " + entered.name, Debugger.Tag.LISTENER);
        entered = null;
    }

    void mouseDragged(MouseEvent mouseEvent) {
        for (MouseListener mouseListener : entered.mouseListeners) {
            mouseListener.mouseDragged(mouseEvent);
        }
        Debugger.log("Dragged " + entered.name, Debugger.Tag.LISTENER);
    }

    void mouseMoved(MouseEvent mouseEvent) {
        if(entered != null && entered.pressed){
            return;
        }
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        if(entered != null) {
            if (!entered.isInside(mouseX, mouseY)) {
                for (MouseListener mouseListener : entered.mouseListeners) {
                    mouseListener.mouseExited(mouseEvent);
                }
                Debugger.log("Exited " + entered.name, Debugger.Tag.LISTENER);
                entered = this;
            }
        }
        for(Visual v: children) {
            if(v.isInside(mouseX, mouseY)) {
                v.mouseMoved(mouseEvent);
                return;
            }
        }
        if (this != entered && entered != null) {
            for (MouseListener mouseListener : entered.mouseListeners) {
                mouseListener.mouseExited(mouseEvent);
            }
            entered = this;
            for (MouseListener mouseListener : mouseListeners) {
                mouseListener.mouseEntered(mouseEvent);
            }
        }
        else {
            for (MouseListener mouseListener : mouseListeners) {
                mouseListener.mouseMoved(mouseEvent);
            }
        }
        Debugger.log("Moved " + this.name, Debugger.Tag.LISTENER);
    }

    void keyPressed(KeyEvent keyEvent) {
        if(focused == null) {
            return;
        }
        for(KeyListener keyListener: focused.keyListeners) {
            keyListener.keyPressed(keyEvent);
        }
        Debugger.log("Key " + keyEvent.paramString() + " Pressed " + focused.name, Debugger.Tag.LISTENER);
    }

    void keyReleased(KeyEvent keyEvent) {
        if(focused == null) {
            return;
        }
        for(KeyListener keyListener: focused.keyListeners) {
            keyListener.keyReleased(keyEvent);
        }
        Debugger.log("Key " + keyEvent.paramString() + " Released " + focused.name, Debugger.Tag.LISTENER);
    }

    void keyTyped(KeyEvent keyEvent) {
        if(focused == null) {
            return;
        }
        for(KeyListener keyListener: focused.keyListeners) {
            keyListener.keyTyped(keyEvent);
        }
        Debugger.log("Key " + keyEvent.paramString() + " Typed " + focused.name, Debugger.Tag.LISTENER);
    }

    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if(focused != null) {
            for(MouseWheelListener mouseWheelListener: focused.mouseWheelListeners) {
                mouseWheelListener.mouseWheelMoved(mouseWheelEvent);
            }
            Debugger.log("Wheel Moved " + focused.name, Debugger.Tag.LISTENER);
        }
    }

    /*--------------------------------------------------------------------
                            Helper Methods
    ---------------------------------------------------------------------*/

    private void initializeImageBuffer(){
        if(this.width <= 0 || this.height <= 0) {
            return;
        }
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        clearImageBuffer();
    }

    private void clearImageBuffer() {
        if(imageBuffer == null) {
            return;
        }
        Graphics2D g = this.imageBuffer.createGraphics();

        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.dispose();
    }

    private void activate() {
        this.active = true;
        for(Visual child: children) {
            child.activate();
        }
    }

    private void deactivate() {
        this.active = false;
        this.imageBuffer = null;
        for(Visual child: children) {
            child.deactivate();
        }
    }

    private boolean isInside(int x, int y) {
        return x > absoluteX && x < absoluteX +  width && y > absoluteY && y < absoluteY + height;
    }

    private void calculateAbsoluteLocation() {
        if(parent == null) {
            absoluteX = locationX;
            absoluteY = locationY;
        }
        else {
            absoluteX = locationX + parent.absoluteX;
            absoluteY = locationY + parent.absoluteY;
            for(Visual v: children) {
                v.calculateAbsoluteLocation();
            }
        }
    }

    private void propagateDirt() {
        dirty = true;
        if(parent != null) {
            parent.propagateDirt();
        }
    }
}
