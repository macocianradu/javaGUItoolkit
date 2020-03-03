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

        this.active = this instanceof Window;
    }


    /*--------------------------------------------------------------------
                    Attributes Methods
    ---------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setSize(Integer width, Integer height){
        this.width = width;
        this.height = height;

        initializeImageBuffer(width, height);

        this.revalidate();
    }

    public int getLocationX(){
        return this.locationX;
    }

    public int getLocationY(){
        return this.locationY;
    }

    public void setLocation(Integer x, Integer y){
        this.locationX = x;
        this.locationY = y;
        this.revalidate();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.revalidate();
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.revalidate();
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
        child.imageBuffer = new BufferedImage(child.getWidth(), child.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        child.active = true;
        child.revalidate();
    }

    public void removeVisual(Visual child) {
        this.children.remove(child);
        child.setParent(null);
        child.setSize(0, 0);
        child.setLocation(0, 0);
        child.imageBuffer = null;
        child.active = false;
        revalidate();
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

    public void callRepaint()
    {
        this.paint(this.imageBuffer);
        this.parent.revalidate();
    }

    public void revalidate() {
        if(!this.active){
            return;
        }
        initializeImageBuffer(width, height);
        this.paint(imageBuffer);
        for(Visual v:children){
            this.imageBuffer.getGraphics().drawImage(v.imageBuffer, v.locationX, v.locationY, null);
        }
        if(!(this instanceof Window)){
            this.parent.revalidate();
        }
        else{
            Window window = (Window)this;
            window.setFrameImageBuffer(this.imageBuffer);
        }
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
        }
    }

    void mouseReleased(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() + offsetX;
        int mouseY = mouseEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mouseReleased(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseReleased(mouseEvent);
            }
        }
    }

    void mousePressed(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() + offsetX;
        int mouseY = mouseEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mousePressed(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mousePressed(mouseEvent);
            }
        }
    }

    void mouseEntered(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() + offsetX;
        int mouseY = mouseEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mouseEntered(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseEntered(mouseEvent);
            }
        }
    }

    void mouseExited(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() + offsetX;
        int mouseY = mouseEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mouseExited(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseExited(mouseEvent);
            }
        }
    }

    void mouseDragged(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() + offsetX;
        int mouseY = mouseEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mouseDragged(mouseEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseDragged(mouseEvent);
            }
        }
    }

    void mouseMoved(MouseEvent mouseEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseEvent.getX() + offsetX;
        int mouseY = mouseEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mouseMoved(mouseEvent, offsetX + v.locationX, offsetY + locationY);
            }
        }
        if(front) {
            for(MouseListener mouseListener: mouseListeners) {
                mouseListener.mouseMoved(mouseEvent);
            }
        }
    }

    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent, int offsetX, int offsetY) {
        boolean front = true;
        int mouseX = mouseWheelEvent.getX() + offsetX;
        int mouseY = mouseWheelEvent.getY() + offsetY;
        for(Visual v: children) {
            if(mouseX > v.getLocationX() &&
                    mouseY < v.getLocationY() &&
                    mouseX < v.getWidth() &&
                    mouseY > v.getHeight()) {
                front = false;
                v.mouseWheelMoved(mouseWheelEvent, offsetX + v.locationX, offsetY + v.locationY);
            }
        }
        if(front) {
            for(MouseWheelListener mouseWheelListener: mouseWheelListeners) {
                mouseWheelListener.mouseWheelMoved(mouseWheelEvent);
            }
        }
    }

    /*--------------------------------------------------------------------
                            Helper Methods
    ---------------------------------------------------------------------*/
    private void initializeImageBuffer(Integer width, Integer height){
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        this.imageBuffer.getGraphics().setColor(backgroundColor);
        this.imageBuffer.getGraphics().fillRect(0, 0, width, height);
    }
}
