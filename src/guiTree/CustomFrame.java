package guiTree;

import guiTree.Helper.Debugger;
import guiTree.Helper.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class CustomFrame extends JFrame {
    private BufferedImage imageBuffer;
    private Window parentWindow;
    private final int resizeDelta = 5;

    public CustomFrame(Window parent)
    {
        this("", parent);
    }

    public CustomFrame(String name, Window parent)
    {
        super(name);
        this.parentWindow = parent;
        this.addMouseMotionListener(new MouseEventGetter(parent, resizeDelta));
        this.addMouseListener(new MouseEventGetter(parent, resizeDelta));
        this.addKeyListener(new KeyEventGetter(parent));
        this.addMouseWheelListener(new MouseWheelGetter(parent));
        MouseResizeListener listener = new MouseResizeListener();
        this.addMouseMotionListener(listener);
        this.addMouseListener(listener);
    }

    public void setImageBuffer(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    @Override
    public void paint(Graphics g) {
        Timer timer = new Timer();
        timer.startTiming();
        g.drawImage(imageBuffer, 0, 0, this.getWidth(), this.getHeight(), null);
        Debugger.log("AWT time: " + timer.stopTiming(), Debugger.Tag.PAINTING);
    }

    private class MouseResizeListener implements MouseMotionListener, MouseListener {
        private int startX;
        private int startY;
        private boolean resizing = false;
        private boolean changedCursor = false;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.startX = e.getXOnScreen();
            this.startY = e.getYOnScreen();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            resizing = false;
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(getCursor().getType() != Cursor.DEFAULT_CURSOR && !this.resizing){
                this.resizing = true;
                this.resize(e);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if(e.getX() < resizeDelta || e.getX() > getWidth() - resizeDelta ||
                    e.getY() < resizeDelta || e.getY() > getHeight() - resizeDelta) {
                this.setResizeCursor(e);
                changedCursor = true;
            }
            else{
                if(changedCursor) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    changedCursor = false;
                }
            }
        }

        private void setResizeCursor(MouseEvent e){
            if(e.getX() <= resizeDelta){
                if(e.getY() <= resizeDelta){
                    setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                }
                else if(e.getY() >= getHeight() - resizeDelta){
                    setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                }
                else{
                    setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                }
            }
            else if(e.getX() >= getWidth() - resizeDelta){
                if(e.getY() <= resizeDelta){
                    setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                }
                else if(e.getY() >= getHeight() - resizeDelta){
                    setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                }
                else{
                    setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                }
            }
            else{
                if(e.getY() <= resizeDelta){
                    setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                }
                else if(e.getY() >= getHeight() - resizeDelta){
                    setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                }
            }
        }

        private void resize(MouseEvent e){
            switch (getCursor().getType()){
                case Cursor.N_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth(), getHeight() + startY - e.getYOnScreen());
                    parentWindow.setLocation(getX(), e.getYOnScreen());
                    startY = e.getYOnScreen();
                    break;
                case Cursor.NE_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth() + e.getXOnScreen() - startX, getHeight() + startY - e.getYOnScreen());
                    parentWindow.setLocation(getX(), e.getYOnScreen());
                    startX = e.getXOnScreen();
                    startY = e.getYOnScreen();
                    break;
                case Cursor.E_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth() + e.getXOnScreen() - startX, getHeight());
                    startX = e.getXOnScreen();
                    break;
                case Cursor.SE_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth() + e.getXOnScreen() - startX, getHeight() + e.getYOnScreen() - startY);
                    startX = e.getXOnScreen();
                    startY = e.getYOnScreen();
                    break;
                case Cursor.S_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth(), getHeight() + e.getYOnScreen() - startY);
                    startY = e.getYOnScreen();
                    break;
                case Cursor.SW_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth() + startX - e.getXOnScreen(), getHeight() + e.getYOnScreen() - startY);
                    parentWindow.setLocation(e.getXOnScreen(), getY());
                    startX = e.getXOnScreen();
                    startY = e.getYOnScreen();
                    break;
                case Cursor.W_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth() + startX - e.getXOnScreen(), getHeight());
                    parentWindow.setLocation(e.getXOnScreen(), getY());
                    startX = e.getXOnScreen();
                    break;
                case Cursor.NW_RESIZE_CURSOR:
                    parentWindow.setSize(getWidth() + startX - e.getXOnScreen(), getHeight() + startY - e.getYOnScreen());
                    parentWindow.setLocation(e.getXOnScreen(), e.getYOnScreen());
                    startX = e.getXOnScreen();
                    startY = e.getYOnScreen();
                    break;
            }
            this.resizing = false;
        }
    }
}
