package guiTree;

import guiTree.Animations.AnimationInterface;
import guiTree.Components.Decorations.*;
import guiTree.Components.Decorations.Placers.*;
import guiTree.Components.DropDown;
import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Helper.Point4;
import guiTree.Helper.Timer;
import guiTree.events.KeyListener;
import guiTree.events.MouseListener;
import guiTree.events.MouseWheelListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Visual {
    /*--------------------------------------------------------------------
                            Constant Values
    ---------------------------------------------------------------------*/

    public static final int SIZE_CHANGED = 1;
    public static final int LOCATION_CHANGED = 2;
    public static final boolean GPU_DISABLED = false;
    public static final boolean GPU_ENABLED = true;

    /*--------------------------------------------------------------------
                            Tree Elements
    ---------------------------------------------------------------------*/

    private List<Visual> children;
    private Visual parent;
    private Image imageBuffer;
    private String name;
    private List<MouseListener> mouseListeners;
    private List<MouseWheelListener> mouseWheelListeners;
    private List<KeyListener> keyListeners;
    private static boolean useGPU = GPU_DISABLED;

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
    private Placer locationPlacer;
    private Integer absoluteX;
    private Integer absoluteY;
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
    private static ReentrantLock validating = new ReentrantLock();
    private Boolean hardwareAccelerated;
    private Boolean disabled;

    /*--------------------------------------------------------------------
                        Constructors
    ---------------------------------------------------------------------*/

    public Visual() {
        this(1, 1);
    }

    public Visual(int width, int height) {
        children = new ArrayList<>();
        mouseWheelListeners = new ArrayList<>();
        mouseListeners = new ArrayList<>();
        keyListeners = new ArrayList<>();
        parent = null;
        name = "";
        backgroundColor = Color.WHITE;
        paintColor = Color.WHITE;
        foregroundColor = Color.BLUE;
        fontColor = Color.BLACK;
        accentColor = Color.BLUE;

        dirty = true;
        active = this instanceof Window;
        pressed = false;
        attributeMap = new HashMap<>();

        this.width = width;
        this.height = height;
        relativeWidth = -1.0f;
        relativeHeight = -1.0f;
        locationPlacer = new GeneralPlacer();
        locationPlacer.setElementSize(width, height);
        locationPlacer.setLocation(0, 0);
        locationPlacer.setRelativeLocation(-1.0f, -1.0f);

        locationX = 0;
        locationY = 0;
        absoluteX = 0;
        absoluteY = 0;

        disabled = false;

        hardwareAccelerated = useGPU;
    }

    /*--------------------------------------------------------------------
                    Attributes Setters
    ---------------------------------------------------------------------*/

    public void setName(String name) {
        this.name = name;
    }

    public void setSize() {
        if(parent != null) {
            if(relativeWidth >= 0.0) {
                width = Math.round(relativeWidth * parent.width);
            }
            if(relativeHeight >= 0.0) {
                height = Math.round(relativeHeight * parent.height);
            }

        }
        initializeImageBuffer();
        locationPlacer.setElementSize(width, height);
        setLocation();

        for(Visual v: children) {
            if(v.relativeHeight > 0.0 || v.relativeWidth > 0.0) {
                v.setSize();
            }
            if(v.locationPlacer != null) {
                v.locationPlacer.setParentSize(width, height);
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

    public void setMargins(Integer up, Integer down, Integer left, Integer right) {
        locationPlacer.setMargins(up, down, left, right);
        setLocation();
    }

    public void setMargins(Integer margin) {
        locationPlacer.setMargins(margin);
        setLocation();
    }

    public void setLocation() {
        Point2<Integer> location = locationPlacer.getPosition();
        locationX = location.x;
        locationY = location.y;

        calculateAbsoluteLocation();
        update();
        notifyParent(this, LOCATION_CHANGED);
    }

    public void setLocation(Float x, Float y) {
        locationPlacer.setRelativeLocation(x, y);
        setLocation();
    }

    public void setLocation(Integer x, Integer y) {
        locationPlacer.setLocation(x, y);
        setLocation();
    }

    public void setLocationX(Float x) {
        locationPlacer.setRelativeLocation(x, getRelativeLocation().y);
        setLocation();
    }

    public void setLocationY(Float y) {
        locationPlacer.setRelativeLocation(getRelativeLocation().x, y);
        setLocation();
    }

    public void setLocationX(Integer x) {
        setLocation(x, getLocationY());
    }

    public void setLocationY(Integer y) {
        setLocation(getLocationX(), y);
    }

    public void setLocation(String location) {
        location = location.toLowerCase();
        Point4<Integer> margins = locationPlacer.getMargins();
        Point2<Integer> absLocation = locationPlacer.getLocation();
        Point2<Float> relativeLocation = locationPlacer.getRelativeLocation();

        Placer placer = LocationPlacerFactory.getPlacer(location);
        if(placer == null) {
            System.out.println("Not a valid placer " + location);
            return;
        }
        locationPlacer = placer;
        locationPlacer.setLocation(absLocation.x, absLocation.y);
        locationPlacer.setRelativeLocation(relativeLocation.x, relativeLocation.y);
        locationPlacer.setElementSize(width, height);
        locationPlacer.setMargins(margins.a, margins.b, margins.c, margins.d);
        if(parent != null) {
            locationPlacer.setParentSize(parent.width, parent.height);
        }
        setLocation();
    }

    public void setLocation(Placer placer) {
        locationPlacer = placer;
        setLocation();
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setFontSize(Float size) {
        if(font == null) {
            font = new Font("TimesRoman", Font.BOLD, Math.round(size));
            return;
        }
        font = font.deriveFont(size);
    }

    public void setFont(String font, Integer style) {
        setFont(font, 10f, style);
    }

    public void setFont(String font, Float size) {
        setFont(font, size, Font.PLAIN);
    }

    public void setFont(String font, Float size, Integer style) {
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/" + font + ".ttf");
            assert fontStream != null;
            this.font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            this.font = this.font.deriveFont(style, size);
            update();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
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

    public void setHardwareAcceleratedOnElement(Boolean hardwareAccelerated) {
        this.hardwareAccelerated = hardwareAccelerated;
        children.forEach(f -> f.setHardwareAcceleratedOnElement(hardwareAccelerated));
        initializeImageBuffer();
        update();
    }

    public static void setHardwareAcceleration(Boolean gpu) {
        useGPU = gpu;
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.accthreshold", "0");
    }

    public void setAttribute(String attribute, String value) {
        attributeMap.put(attribute, value);
    }

    public void setDisable(Boolean disable) {
        disabled = disable;
        if(disable) {
            setPaintColor(Color.LIGHT_GRAY);
        }
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

    public Point2<Integer> getSize(){
        return new Point2<>(width, height);
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

    public Point4<Integer> getMargins() {
        return locationPlacer.getMargins();
    }

    public Point2<Integer> getLocation() {
        return new Point2<>(locationX, locationY);
    }

    public Point2<Float> getRelativeLocation() {return locationPlacer.getRelativeLocation();}

    public int getAbsoluteX() {
        return absoluteX;
    }

    public int getAbsoluteY() {
        return absoluteY;
    }

    public Point2<Integer> getAbsoluteLocation() {
        return new Point2<>(absoluteX, absoluteY);
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

    public boolean isElementHardwareAccelerated() {
        return hardwareAccelerated;
    }

    public boolean isHardwareAccelerationEnabled() {
        return useGPU;
    }

    public String getAttribute(String attribute) {
        return attributeMap.get(attribute);
    }

    public Boolean isDisabled() {
        return disabled;
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
        children.add(child);
        child.setParent(this);
        child.setLocation();
        child.setSize();

        if(active) {
            child.activate();
        }
        child.update();
    }

    public void addVisual(Decoration decoration) {
        children.add(decoration);
        ((Visual)decoration).setParent(this);
        decoration.setLocation();
        decoration.setSize();

        if(active) {
            ((Visual)decoration).activate();
        }
        decoration.update();
    }

    public void removeVisual(Visual child) {
        if(child == null) {
            return;
        }
        this.children.remove(child);
        child.parent = null;
        child.imageBuffer = null;
        child.deactivate();
        update();
    }

    public void removeAllVisuals() {
        children.forEach(f -> {
            f.parent = null;
            f.imageBuffer = null;
            f.deactivate();
        });
        children.clear();

    }

    private void setParent(Visual parent) {
        locationPlacer.setParentSize(parent.width, parent.height);
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
        if (parent != null) {
            parent.addAnimation(animation);
        }
    }

    public void removeAnimation(AnimationInterface animation) {
        if(parent != null) {
            parent.removeAnimation(animation);
        }
    }

    public void removeAllAnimations() {
        if(parent != null) {
            parent.removeAllAnimations();
        }
    }

    public void repaint() {
        Debugger.log("Called repaint from " + name, Debugger.Tag.PAINTING);
        validating.lock();
        if(dirty && active) {
            revalidate();
        }
        validating.unlock();
    }

    private void revalidate() {
        Debugger.log("Revalidating " + name, Debugger.Tag.PAINTING);
        Timer timer = new Timer();

        timer.startTiming();

        clearImageBuffer();
        paint(imageBuffer);

        int size = children.size();
        for (int i = 0; i < size; i++) {
            Visual v = children.get(i);
            if (v.dirty && v.active) {
                v.revalidate();
            }
            Graphics2D g = (Graphics2D) imageBuffer.getGraphics();
            g.drawImage(v.imageBuffer, v.locationX, v.locationY, null);
            g.dispose();
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

    public void setCursor(Cursor cursor) {
        if(parent != null) {
            parent.setCursor(cursor);
        }
    }

    public void paint(Image imageBuffer) {
    }

    public void bringToFront(Visual v) {
        if(children.contains(v)) {
            children.remove(v);
            children.add(v);
        }
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
        Debugger.log("Clicked " + entered.name, Debugger.Tag.LISTENER);
        if(entered.disabled) {
            return;
        }
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseClicked(entered.createMouseEvent(mouseEvent));
        }
    }

    void mouseReleased(MouseEvent mouseEvent) {
        entered.pressed = false;
        Debugger.log("Released " + entered.name, Debugger.Tag.LISTENER);
        if(entered.disabled) {
            return;
        }
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mouseReleased(entered.createMouseEvent(mouseEvent));
        }
    }

    void mousePressed(MouseEvent mouseEvent) {
        Debugger.log("Pressed " + entered.name, Debugger.Tag.LISTENER);
        entered.pressed = true;
        if(focused != null) {
            focused.update();
        }
        focused = entered;
        if(entered.disabled) {
            return;
        }
        for(MouseListener mouseListener: entered.mouseListeners) {
            mouseListener.mousePressed(entered.createMouseEvent(mouseEvent));
        }
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
        fireEnteredListener(entered, mouseEvent);
    }

    void mouseExited(MouseEvent mouseEvent) {
        if(entered == null) {
            return;
        }
        if(entered.pressed) {
            return;
        }
        fireExitedListener(entered, mouseEvent);
        entered = null;
    }

    void mouseDragged(MouseEvent mouseEvent) {
        Debugger.log("Dragged " + entered.name, Debugger.Tag.LISTENER);
        if(entered.disabled) {
            return;
        }
        for (MouseListener mouseListener : entered.mouseListeners) {
            mouseListener.mouseDragged(entered.createMouseEvent(mouseEvent));
        }
    }

    void mouseMoved(MouseEvent mouseEvent) {
        if(entered != null && entered.pressed){
            return;
        }
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        if(entered != null) {
            if (!entered.isInside(mouseX, mouseY)) {
                fireExitedListener(entered, mouseEvent);
                entered = this;
            }
        }
        for(int i = children.size() - 1; i >=0; i--) {
            Visual v = children.get(i);
            if(v.isInside(mouseX, mouseY)) {
                v.mouseMoved(mouseEvent);
                return;
            }
        }
        if (this != entered && entered != null) {
            fireExitedListener(entered, mouseEvent);
            entered = this;
            fireEnteredListener(entered, mouseEvent);
        }
        else {
            fireMovedListener(this, mouseEvent);
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

    public void requestFocus() {
        focused = this;
    }

    /*--------------------------------------------------------------------
                            Helper Methods
    ---------------------------------------------------------------------*/

    private void initializeImageBuffer(){
        if(this.width <= 0 || this.height <= 0) {
            return;
        }
        if(useGPU == GPU_ENABLED && hardwareAccelerated) {
            imageBuffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
            imageBuffer.setAccelerationPriority(0);
            clearImageBuffer();
            return;
        }
        imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        clearImageBuffer();
    }

    private void clearImageBuffer() {
        if(imageBuffer == null) {
            return;
        }
        Graphics2D g = (Graphics2D) imageBuffer.getGraphics();

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

    private void fireExitedListener(Visual v, MouseEvent mouseEvent) {
        Debugger.log("Exited " + v.name, Debugger.Tag.LISTENER);
        if(v.disabled) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        for(MouseListener listener: v.mouseListeners) {
            listener.mouseExited(mouseEvent);
        }
    }

    private void fireEnteredListener(Visual v, MouseEvent mouseEvent) {
        Debugger.log("Entered " + v.name, Debugger.Tag.LISTENER);
        if(v.disabled) {
            InputStream iconStream = getClass().getClassLoader().getResourceAsStream("icons/forbidden_red.png");
            try {
                assert iconStream != null;
                BufferedImage forbiddenIcon = ImageIO.read(iconStream);
                BufferedImage actualCursor = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                Graphics2D cursorGraphics = actualCursor.createGraphics();
                cursorGraphics.setColor(new Color(0, 0, 0, 0));
                cursorGraphics.fillRect(0, 0, actualCursor.getWidth(), actualCursor.getHeight());
                cursorGraphics.drawImage(forbiddenIcon, 0, 0, null);
                cursorGraphics.dispose();
                setCursor(Toolkit.getDefaultToolkit().createCustomCursor(actualCursor, new Point(0, 0), "forbidden cursor"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        for(MouseListener listener: v.mouseListeners) {
            listener.mouseEntered(mouseEvent);
        }
    }

    private void fireMovedListener(Visual v, MouseEvent mouseEvent) {
        if(v.disabled) {
            return;
        }
        for(MouseListener listener: v.mouseListeners) {
            listener.mouseMoved(mouseEvent);
        }
    }

    public void update() {
        validating.lock();
        if(!dirty) {
            dirty = true;
        }
        validating.unlock();

        if(parent != null) {
            parent.update();
        }
    }
}
