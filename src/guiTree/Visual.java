package guiTree;

import guiTree.events.KeyListener;
import guiTree.events.MouseListener;
import guiTree.events.MouseWheelListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Visual {
    /*--------------------------------------------------------------------
                            Constant Values
    ---------------------------------------------------------------------*/
    public static final int SIZE_CHANGED = 1;
    public static final int LOCATION_CHANGED = 2;

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
    private Integer locationX;
    private Integer locationY;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color fontColor;
    private Boolean active;
    private Boolean dirty;
    private static Visual entered;
    private Boolean focused;
    private Boolean pressed;


    /*--------------------------------------------------------------------
                        Constructors
    ---------------------------------------------------------------------*/

    public Visual() {
        this(0, 0);
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

        this.dirty = true;
        this.active = this instanceof Window;
        this.focused = false;
        this.pressed = false;

        this.width = width;
        this.height = height;

        this.locationX = 0;
        this.locationY = 0;
    }

    /*--------------------------------------------------------------------
                    Attributes Setters
    ---------------------------------------------------------------------*/

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Integer width, Integer height) {
        this.width = width;
        this.height = height;

        initializeImageBuffer();

        this.dirty = true;
        this.notifyParent(SIZE_CHANGED);
    }

//    public void setSize(Float width, Float height) {
//        this.width = Math.round(this.parent.width * width);
//        this.height = Math.round(this.parent.height * height);
//    }

    public void setLocation(Integer x, Integer y) {
        this.locationX = x;
        this.locationY = y;
        this.dirty = true;
        notifyParent(LOCATION_CHANGED);
    }

    public void setLocationX(Integer x) {
        this.locationX = x;
    }

    public void setLocationY(Integer y) {
        this.locationY = y;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.dirty = true;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.dirty = true;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    private void calculateInitialSize() {
        if(this.width <= 0) {
            this.width = 1;
        }
        if(this.height <= 0){
            this.height = 1;
        }
    }

    private void calculateInitialLocation() {
        if(this.locationX <= 0) {
            this.locationX = 0;
        }
        if(this.locationY <= 0){
            this.locationY = 0;
        }
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

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getFontColor() {
        return fontColor;
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
        child.calculateInitialLocation();
        child.calculateInitialSize();

        if(this.active) {
            child.activate();
        }
    }

    public void removeVisual(Visual child) {
        this.children.remove(child);
        child.setParent(null);
        child.setSize(0, 0);
        child.setLocation(0, 0);
        child.imageBuffer = null;
        child.deactivate();
        this.dirty = true;
    }

    private void setParent(Visual parent) {
        this.parent = parent;
    }

    public void handleNotification(int notify) {

    }

    public void notifyParent(int notify) {
        if(parent != null) {
            this.parent.handleNotification(notify);
        }
    }

    private void repaint() {
        this.paint(imageBuffer);
        if(this.dirty) {
            this.revalidate();
            return;
        }
        for(Visual v: children) {
            v.repaint();
            imageBuffer.getGraphics().drawImage(v.imageBuffer, v.locationX, v.locationY, null);
        }
    }

    public void revalidate() {
        if(!this.active){
            return;
        }
        clearImageBuffer();
        this.paint(imageBuffer);
        for (Visual v : children) {
            v.repaint();
            this.imageBuffer.getGraphics().drawImage(v.imageBuffer, v.locationX, v.locationY, null);
        }
        this.dirty = false;
        if(!(this instanceof Window)){
            this.parent.revalidate();
            return;
        }
        Window window = (Window)this;
        window.setFrameImageBuffer(imageBuffer);
        window.repaint();
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
        dirty = true;
        entered.focused = true;
    }

    void mouseReleased(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseReleased(mouseEvent);
        }
        dirty = true;
        entered.pressed = false;
    }

    void mousePressed(MouseEvent mouseEvent) {
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mousePressed(mouseEvent);
        }
        dirty = true;
        entered.pressed = true;
    }

    void mouseEntered(MouseEvent mouseEvent, int offsetX, int offsetY) {
        if(entered != null && entered.pressed){
            return;
        }
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()){
                front = false;
                v.mouseEntered(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            entered = this;
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseEntered(mouseEvent);
            }
            dirty = true;
        }
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
        entered = null;
        dirty = true;
    }

    void mouseDragged(MouseEvent mouseEvent) {
        for (MouseListener mouseListener : entered.mouseListeners) {
            mouseListener.mouseDragged(mouseEvent);
        }
        entered.dirty = true;
    }

    void mouseMoved(MouseEvent mouseEvent, int offsetX, int offsetY) {
        if(entered != null && entered.pressed){
            return;
        }
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        if(entered != null) {
            if (!entered.isInside(mouseEvent.getX(), mouseEvent.getY())) {
                for (MouseListener mouseListener : entered.mouseListeners) {
                    mouseListener.mouseExited(mouseEvent);
                }
                entered = this;
            }
        }
        for(Visual v: children) {
            if(v.isInside(mouseX, mouseY)) {
                front = false;
                v.mouseMoved(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            if(this.isInside(mouseEvent.getX(), mouseEvent.getY())) {
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
            }
            dirty = true;
        }
    }

    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent, int offsetX, int offsetY) {
        if(focused) {
            for(MouseWheelListener mouseWheelListener: mouseWheelListeners) {
                mouseWheelListener.mouseWheelMoved(mouseWheelEvent);
            }
        }
    }

    /*--------------------------------------------------------------------
                            Helper Methods
    ---------------------------------------------------------------------*/
    private void initializeImageBuffer(){
        if(this.width == 0 || this.height == 0) {
            return;
        }
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        clearImageBuffer();
    }

    private void clearImageBuffer() {
        Graphics g = this.imageBuffer.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    private void activate() {
        this.active = true;
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(Visual child: children) {
            child.activate();
        }
        this.dirty = true;
    }

    private void deactivate() {
        this.active = false;
        this.imageBuffer = null;
        for(Visual child: children) {
            child.deactivate();
        }
    }

    private boolean isInside(int x, int y) {
        return x > locationX && x < locationX +  width && y > locationY && y < locationY + height;
    }
}
