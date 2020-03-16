package guiTree;

import guiTree.Components.TitleBar;
import guiTree.Helper.Point2d;
import guiTree.events.MouseAdapter;

import javax.tools.Tool;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;

public class Window extends Visual {
    public CustomFrame frame;
    private TitleBar titleBar;

    public Window()
    {
        super();
        this.frame = new CustomFrame(this);
        this.setUndecorated(true);
        this.addWindowStateListener(e -> {
            this.setSize(getWidth(), getHeight());
            revalidate();
        });
    }

    @Override
    public void setSize(Integer width, Integer height)
    {
        this.frame.setSize(width, height);
        super.setSize(width, height);
        if(this.titleBar != null) {
            this.titleBar.setSize(this.getWidth(), titleBar.getHeight());
        }
        revalidate();
    }

    public void setFrameImageBuffer(BufferedImage imageBuffer){
        this.frame.setImageBuffer(imageBuffer);
        this.frame.repaint();
    }

    public void repaint() {
        this.frame.repaint();
    }

    public void setSize(Dimension dimension) {
        this.setSize(dimension.width, dimension.height);
    }

    public void setVisible(Boolean visible)
    {
        frame.setVisible(visible);
    }

    public void addWindowListener(WindowListener listener)
    {
        frame.addWindowListener(listener);
    }

    public void addWindowStateListener(WindowStateListener listener)
    {
        frame.addWindowStateListener(listener);
    }

    public void dispose()
    {
        frame.dispose();
    }

    public void repaint(long tm) {
        frame.repaint(tm);
    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.setSize(this.getWidth(), titleBar.getHeight());

        if(this.getTitleBar() != null) {
            this.removeVisual(this.titleBar);
        }

        this.titleBar = titleBar;
        this.addVisual(titleBar, 0, 0);
        this.titleBar.addMouseListener(new TitleBarMouseListener());
    }

    public TitleBar getTitleBar() {
        return this.titleBar;
    }

    public void setPositionRelativeTo(Component c){
        frame.setLocationRelativeTo(c);
    }

    public void setPositionRelativeTo(){
        frame.setLocationRelativeTo(null);
    }

    public void setUndecorated(Boolean undecorated){frame.setUndecorated(undecorated);}

    public void setState(int state) {
        frame.setState(state);
    }

    private void moveTo(int x, int y) {
        this.frame.setLocation(x, y);
    }

    @Override
    public void handleNotification(int notify) {
        switch(notify) {
            case TitleBar.CLOSE:
                dispose();
                break;
            case TitleBar.MINIMIZE:
                setState(Frame.ICONIFIED);
                break;
            case TitleBar.MAXIMIZE:
                Rectangle screenBounds = frame.getGraphicsConfiguration().getBounds();
                this.setSize(screenBounds.width, screenBounds.height);
                this.moveTo(screenBounds.x, screenBounds.y);
                setState(Frame.MAXIMIZED_BOTH);
                break;
        }
    }

    private class WindowMouseListener extends MouseAdapter{
        private Boolean resizing;
        private Boolean moving;
        private Point2d initialLocation;

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            this.initialLocation = new Point2d(mouseEvent.getX(), mouseEvent.getY());
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            moving = false;
            resizing = false;
        }
    }


    private class TitleBarMouseListener extends MouseAdapter {
        private Boolean moving = false;
        private Point2d initialLocation;

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            if(moving) {
                moveTo(mouseEvent.getXOnScreen() - initialLocation.x, mouseEvent.getYOnScreen() - initialLocation.y);
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            moving = true;
            initialLocation = new Point2d(mouseEvent.getX(), mouseEvent.getY());
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            moving = false;
        }
    }
}
