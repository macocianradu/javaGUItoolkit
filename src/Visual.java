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
    private int width;
    private int height;
    private int locationX;
    private int locationY;
    private Color backgroundColor;
    private Color foregroundColor;
    private Boolean valid;


    /*--------------------------------------------------------------------
                        Constructors
    ---------------------------------------------------------------------*/
    public Visual() {
        this.children = new ArrayList<>();
        this.parent = null;
        this.backgroundColor = Color.WHITE;
        this.foregroundColor = Color.BLACK;
        valid = false;
    }


    /*--------------------------------------------------------------------
                    Attributes Methods
    ---------------------------------------------------------------------*/
    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;

        initializeImageBuffer(width, height);

        this.valid = false;
        this.revalidate();
    }

    public int getLocationX(){
        return this.locationX;
    }

    public int getLocationY(){
        return this.locationY;
    }

    public void setLocation(int x, int y){
        this.locationX = x;
        this.locationY = y;
        this.valid = false;
        this.revalidate();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.valid = false;
        this.revalidate();
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.valid = false;
        this.revalidate();
    }

    private void calculateInitialSize() {
        this.width = 20;
        this.height = 20;
    }

    private void calculateInitialLocation(){
        this.locationX = 200;
        this.locationY = 200;
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
        child.valid = false;

        this.revalidate();
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
        initializeImageBuffer(width, height);
        this.paint(imageBuffer);
        for(Visual v:children){
            if(!v.valid){
                v.paint(v.imageBuffer);
            }
            this.imageBuffer.getGraphics().drawImage(v.imageBuffer, v.locationX, v.locationY, null);
            v.valid = true;
        }
        this.valid = true;
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

    private void initializeImageBuffer(int width, int height){
        this.imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        this.imageBuffer.getGraphics().setColor(backgroundColor);
        this.imageBuffer.getGraphics().fillRect(0, 0, width, height);
        this.imageBuffer.getGraphics().setColor(foregroundColor);
    }
}
