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
    private Boolean active;
    private Boolean dirty;
    private Boolean pressed;


    /*--------------------------------------------------------------------
                        Constructors
    ---------------------------------------------------------------------*/
    public Visual() {
        this.children = new ArrayList<>();
        this.mouseWheelListeners = new ArrayList<>();
        this.mouseListeners = new ArrayList<>();
        this.keyListeners = new ArrayList<>();
        this.parent = null;
        this.backgroundColor = Color.WHITE;
        this.foregroundColor = Color.BLACK;

        this.dirty = true;
        this.active = this instanceof Window;
    }


    /*--------------------------------------------------------------------
                    Attributes Setters
    ---------------------------------------------------------------------*/

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Integer width, Integer height){
        this.width = width;
        this.height = height;

        initializeImageBuffer();

        this.dirty = true;
    }

    public void setLocation(Integer x, Integer y){
        this.locationX = x;
        this.locationY = y;
        this.dirty = true;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.dirty = true;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.dirty = true;
    }

    private void calculateInitialSize() {
        if(this.width == null) {
            this.width = 20;
        }
        if(this.height == null){
            this.height = 20;
        }
    }

    private void calculateInitialLocation(){
        if(this.locationX == null) {
            this.locationX = 20;
        }
        if(this.locationY == null){
            this.locationY = 50;
        }
    }

    /*--------------------------------------------------------------------
                    Attributes Getters
    ---------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getLocationX(){
        return this.locationX;
    }

    public int getLocationY(){
        return this.locationY;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    /*--------------------------------------------------------------------
                            Tree Methods
    ---------------------------------------------------------------------*/

    public Visual findByName(String name){
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

    private void setParent(Visual parent)
    {
        this.parent = parent;
    }

    private void handleNotification() {

    }

    private void notifyParent()
    {
        this.parent.handleNotification();
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
    void addMouseListener(MouseListener mouseListener) {
        this.mouseListeners.add(mouseListener);
    }

    void removeMouseListener(MouseListener mouseListener) {
        this.mouseListeners.remove(mouseListener);
    }

    void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        this.mouseWheelListeners.add(mouseWheelListener);
    }

    void removeMouseWheelListener(MouseWheelListener mouseWheelListener) {
        this.mouseWheelListeners.remove(mouseWheelListener);
    }

    void addKeyListener(KeyListener keyListener) {
        this.keyListeners.add(keyListener);
    }

    void removeKeyListener(KeyListener keyListener) {
        this.keyListeners.remove(keyListener);
    }

    void mouseClicked(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mouseClicked(mouseEvent, v.locationX + offsetX, v.locationY + offsetY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseClicked(mouseEvent);
            }
            dirty = true;
        }
    }

    void mouseReleased(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mouseReleased(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseReleased(mouseEvent);
            }
            dirty = true;
        }
    }

    void mousePressed(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mousePressed(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mousePressed(mouseEvent);
            }
            dirty = true;
        }
    }

    void mouseEntered(MouseEvent mouseEvent, int offsetX, int offsetY) {
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
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseEntered(mouseEvent);
            }
            dirty = true;
        }
    }

    void mouseExited(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mouseExited(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseExited(mouseEvent);
            }
            dirty = true;
        }
    }

    void mouseDragged(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mouseDragged(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseDragged(mouseEvent);
            }
            dirty = true;
        }
    }

    void mouseMoved(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() - offsetX;
        int mouseY = mouseEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mouseMoved(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseMoved(mouseEvent);
            }
            dirty = true;
        }
    }

    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseWheelEvent.getX() - offsetX;
        int mouseY = mouseWheelEvent.getY() - offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY > v.getLocationY() &&
                    mouseX < v.getWidth() + v.getLocationX() &&
                    mouseY < v.getHeight() + v.getLocationY()) {
                front = false;
                v.mouseWheelMoved(mouseWheelEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseWheelListener mouseWheelListener: mouseWheelListeners) {
                mouseWheelListener.mouseWheelMoved(mouseWheelEvent);
            }
            dirty = true;
        }
    }

    /*--------------------------------------------------------------------
                            Helper Methods
    ---------------------------------------------------------------------*/
    private void initializeImageBuffer(){
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
}
