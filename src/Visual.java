import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Visual {
    private List<Visual> children;
    private Visual parent;
    private BufferedImage imageBuffer;
    private int width;
    private int height;
    private int locationX;
    private int locationY;


    public Visual() {
        this.children = new ArrayList<>();
        this.parent = null;
        this.imageBuffer = new BufferedImage(0, 0, BufferedImage.TYPE_3BYTE_BGR);
    }

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
        
//        if(this.parent != null) {
//            this.imageBuffer = this.parent.getImageBuffer();
//        }
        this.repaint();
    }

    private void calculateInitialSize() {
        this.width = this.parent.getWidth() - this.getLocationX();
        this.height = this.parent.getHeight() - this.getLocationY();
        this.imageBuffer = this.parent.getImageBuffer().getSubimage(this.locationX, this.locationY, this.width, this.height);
    }

    private void calculateInitialLocation(){
        this.locationX = 200;
        this.locationY = 200;
    }

    public void addVisual(Visual child) {
        this.children.add(child);
        child.setParent(this);
        child.calculateInitialLocation();
        child.calculateInitialSize();

        this.repaint();
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

    public void repaint() {
        if(this instanceof Window)
        {
            for(Visual v: children)
            {
                v.paint(this.imageBuffer);
            }
        }
        else{
            this.parent.repaint();
        }
    }

    public void callRepaint()
    {
        this.repaint();
    }

    public int getLocationX(){
        return this.locationX;
    }

    public int getLocationY(){
        return this.locationY;
    }

    public void setLocationX(int locationX){
        this.locationX = locationX;
    }

    public void setLocationY(int locationY){
        this.locationY = locationY;
    }

    public void paint(BufferedImage imageBuffer) {

    }

    public void setImageBuffer(BufferedImage imageBuffer){
        this.imageBuffer = imageBuffer;
    }

    private BufferedImage getImageBuffer() {
        return this.imageBuffer;
    }

    public void triggerBufferChange() {
        for(Visual v: children){
            v.imageBuffer = this.imageBuffer;
            v.triggerBufferChange();
        }
    }
}
