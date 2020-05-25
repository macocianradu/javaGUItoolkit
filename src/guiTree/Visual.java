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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, String> attributeMap;
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
    private Font font;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color accentColor;
    private Color fontColor;
    private Color paintColor;
    private Boolean active;
    private Boolean dirty;
    private static Visual entered;
    private static Visual focused;
    private Boolean pressed;
    private Boolean validating;

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
        this.accentColor = Color.BLUE;

        this.dirty = true;
        this.active = this instanceof Window;
        this.pressed = false;
        this.attributeMap = new HashMap<>();

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

        this.validating = false;
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
        update();
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
        update();
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
        update();
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        update();
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        update();
    }

    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
        update();
    }

    public void setPaintColor(Color paintColor) {
        this.paintColor = paintColor;
        update();
    }

    public void setAttribute(String attribute, String value) {
        attributeMap.put(attribute, value);
    }

    /*--------------------------------------------------------------------
                    Attributes Getters
    ---------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point2<Float> getRelativeSize() {
        return new Point2<>(relativeWidth, relativeHeight);
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public Point2<Integer> getLocation() {
        return new Point2<>(locationX, locationY);
    }

    public int getAbsoluteX() {
        return absoluteX;
    }

    public int getAbsoluteY() {
        return absoluteY;
    }

    public Point2<Integer> getAbsoluteLocation() {
        return new Point2<>(absoluteX, absoluteY);
    }

    public Point2<Float> getRelativeLocation() {
        return new Point2<>(relativeX, relativeY);
    }

    public boolean isFocused() {
        return this == focused;
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

    public Color getPaintColor() {
        return paintColor;
    }

    public String getAttribute(String attribute) {
        return attributeMap.get(attribute);
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
        update();
    }

    public void removeVisual(Visual child) {
        if(child == null) {
            return;
        }
        this.children.remove(child);
        child.setParent(null);
        child.imageBuffer = null;
        child.deactivate();
        update();
    }

    private void setParent(Visual parent) {
        this.parent = parent;
    }

    public void handleNotification(Visual v, int notify) {

    }

    public void notifyParent(Visual v, int notify) {
        if(parent != null) {
            parent.handleNotification(v, notify);
        }
    }

    public void notifyParent(int notify) {
        notifyParent(this, notify);
    }

    public void addAnimation(AnimationInterface animation) {
        animations.add(animation);
    }

    public void removeAnimation(AnimationInterface animation) {
        animations.remove(animation);
    }

    public void removeAllAnimations() {
        animations.clear();
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
        Debugger.log("Revalidating " + name, Debugger.Tag.PAINTING);
        Timer timer = new Timer();

        validating = true;
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
        validating = false;

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

    public void setCursor(Cursor cursor) {
        if(parent != null) {
            parent.setCursor(cursor);
        }
    }

    public void paint(BufferedImage imageBuffer) {
    }

    /*--------------------------------------------------------------------
                            Listener Methods
    ---------------------------------------------------------------------*/

    public void addMouseListener(MouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(MouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    public void removeAllMouseListeners() {
        mouseListeners.clear();
    }

    public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        mouseWheelListeners.add(mouseWheelListener);
    }

    public void removeMouseWheelListener(MouseWheelListener mouseWheelListener) {
        mouseWheelListeners.remove(mouseWheelListener);
    }

    public void removeAllMouseWheelListeners() {
        mouseWheelListeners.clear();
    }

    public void addKeyListener(KeyListener keyListener) {
        keyListeners.add(keyListener);
    }

    public void removeKeyListener(KeyListener keyListener) {
        keyListeners.remove(keyListener);
    }

    public void removeAllKeyListeners() {
        keyListeners.clear();
    }

    void mouseClicked(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseClicked(entered.createMouseEvent(mouseEvent));
        }
        Debugger.log("Clicked " + entered.name, Debugger.Tag.LISTENER);
    }

    void mouseReleased(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseReleased(entered.createMouseEvent(mouseEvent));
        }
        Debugger.log("Released " + entered.name, Debugger.Tag.LISTENER);
        entered.pressed = false;
    }

    void mousePressed(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mousePressed(entered.createMouseEvent(mouseEvent));
        }
        entered.pressed = true;
        if(focused != null) {
            focused.update();
        }
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
            if(v.isInside(mouseX, mouseY)){
                v.mouseEntered(mouseEvent);
                return;
            }
        }
        entered = this;
        for(MouseListener mouseListener: mouseListeners) {
            mouseListener.mouseEntered(createMouseEvent(mouseEvent));
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
            mouseListener.mouseExited(entered.createMouseEvent(mouseEvent));
        }
        Debugger.log("Exited " + entered.name, Debugger.Tag.LISTENER);
        entered = null;
    }

    void mouseDragged(MouseEvent mouseEvent) {
        for (MouseListener mouseListener : entered.mouseListeners) {
            mouseListener.mouseDragged(entered.createMouseEvent(mouseEvent));
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
                    mouseListener.mouseExited(entered.createMouseEvent(mouseEvent));
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
                mouseListener.mouseExited(entered.createMouseEvent(mouseEvent));
            }
            entered = this;
            for (MouseListener mouseListener : mouseListeners) {
                mouseListener.mouseEntered(createMouseEvent(mouseEvent));
            }
            Debugger.log("Entered " + this.name, Debugger.Tag.LISTENER);
        }
        else {
            for (MouseListener mouseListener : mouseListeners) {
                mouseListener.mouseMoved(createMouseEvent(mouseEvent));
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

    private MouseEvent createMouseEvent(MouseEvent mouseEvent) {
        return new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(),
                mouseEvent.getX() - absoluteX, mouseEvent.getY() - absoluteY, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(),
                mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
    }

    public boolean isInside(int x, int y) {
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

    public void update() {
        dirty = true;
        if(parent != null) {
            while(parent.validating){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            parent.update();
        }
    }
}
