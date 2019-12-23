package guiTree;

import java.awt.*;
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


    /*--------------------------------------------------------------------
                        Attributes
    ---------------------------------------------------------------------*/
    private String name;
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
        this.width = 20;
        this.height = 20;
    }

    private void calculateInitialLocation(){
        this.locationX = 20;
        this.locationY = 50;
    }


    /*--------------------------------------------------------------------
                            Tree Methods
    ---------------------------------------------------------------------*/

    public void addVisual(Visual child) {
        this.children.add(child);
        child.setParent(this);
        child.calculateInitialLocation();
        child.calculateInitialSize();
        child.imageBuffer = new BufferedImage(child.getWidth(), child.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        child.active = true;
        child.revalidate();
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
                            Helper Methods
    ---------------------------------------------------------------------*/

    private void initializeImageBuffer(Integer width, Integer height){
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        this.imageBuffer.getGraphics().setColor(backgroundColor);
        this.imageBuffer.getGraphics().fillRect(0, 0, width, height);
        this.imageBuffer.getGraphics().setColor(foregroundColor);
    }
}
