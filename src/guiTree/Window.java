package guiTree;

import guiTree.Components.TitleBar;
import guiTree.Helper.Debugger;
import guiTree.Helper.Point2;
import guiTree.Helper.Timer;
import guiTree.events.KeyListener;
import guiTree.events.MouseAdapter;
import guiTree.Components.Panel;
import guiTree.events.MouseListener;
import guiTree.events.MouseWheelListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends Visual implements Runnable{
    public CustomFrame frame;
    private TitleBar titleBar;
    private Panel mainPanel;
    private Panel contentPanel;
    private Point2<Integer> oldSize;
    private Boolean close;
    private Point2<Integer> oldLocation;

    public Window() {
        this("");
    }

    public Window(String title) {
        super();
        this.frame = new CustomFrame(this);
        this.setUndecorated(true);
        this.addWindowStateListener(e -> {
            this.setSize(getWidth(), getHeight());
            repaint();
        });
        this.mainPanel = new Panel();

        this.setMainPanel(mainPanel);

        BufferedImage icon = null;
        try {
            icon = ImageIO.read(new File("resources\\icons\\square_white.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TitleBar bar = new TitleBar(title, icon);
        bar.setName("TitleBar");
        bar.setBackgroundColor(Color.GRAY);
        this.setTitleBar(bar);
        close = false;
    }

    @Override
    public void setSize(Integer width, Integer height) {
        this.frame.setSize(width, height);
        super.setSize(width, height);
        mainPanel.setSize(width, height);
        if(titleBar != null) {
            titleBar.setSize(this.getWidth(), titleBar.getHeight());
            contentPanel.setSize(width, height - titleBar.getHeight());
        }
        else {
            contentPanel.setSize(width, height);
        }
        Debugger.log("Calling repaint from window set size: ", Debugger.Tag.PAINTING);
        repaint();
    }

    public void setFrameImageBuffer(BufferedImage imageBuffer){
        this.frame.setImageBuffer(imageBuffer);
        this.frame.repaint();
    }

    public void revalidate() {
        Debugger.log("Finished painting", Debugger.Tag.PAINTING);
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
            mainPanel.removeVisual(this.titleBar);
        }

        this.titleBar = titleBar;
        titleBar.setLocation(0, 0);
        contentPanel.setLocation(0, titleBar.getHeight());
        contentPanel.setSize(mainPanel.getWidth(), mainPanel.getHeight() - titleBar.getHeight());
        mainPanel.addVisual(titleBar);
        this.titleBar.addMouseListener(new MouseAdapter() {
            private int startX;
            private int startY;

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                startX = mouseEvent.getX();
                startY = mouseEvent.getY();
            }
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                setLocation(mouseEvent.getXOnScreen() - startX, mouseEvent.getYOnScreen() - startY);
            }
        });
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
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

    public int getState() {
        return frame.getState();
    }

    public void setLocation(int x, int y) {
        this.frame.setLocation(x, y);
    }

    public void setMainPanel(Panel panel) {
        this.removeVisual(mainPanel);
        contentPanel = new Panel();
        contentPanel.setName("ContentPanel");
        if(titleBar != null) {
            panel.addVisual(titleBar);
            contentPanel.setLocation(0, titleBar.getHeight());
            contentPanel.setSize(panel.getWidth(), panel.getHeight() - titleBar.getHeight());
        }
        else {
            contentPanel.setLocation(0, 0);
            contentPanel.setSize(panel.getWidth(), panel.getHeight());
        }
        panel.setName("MainPanel");
        panel.addVisual(contentPanel);
        super.addVisual(panel);
        this.mainPanel = panel;
    }

    public Panel getMainPanel() {
        return this.mainPanel;
    }

    @Override
    public void handleNotification(int notify) {
        switch(notify) {
            case TitleBar.CLOSE: {
                dispose();
                close = true;
                break;
            }
            case TitleBar.MINIMIZE: {
                setState(Frame.ICONIFIED);
                break;
            }
            case TitleBar.MAXIMIZE: {
                Rectangle screenBounds = frame.getGraphicsConfiguration().getBounds();
                oldSize = new Point2<>(getWidth(), getHeight());
                oldLocation = new Point2<>(frame.getX(), frame.getY());
                this.setSize(screenBounds.width, screenBounds.height);
                this.setLocation(screenBounds.x, screenBounds.y);
                setState(Frame.MAXIMIZED_BOTH);
                break;
            }
            case TitleBar.NORMALIZE: {
                this.setSize(oldSize.x, oldSize.y);
                this.setLocation(oldLocation.x, oldLocation.y);
                setState(Frame.NORMAL);
                break;
            }
        }
    }

    @Override
    public void addMouseListener(MouseListener mouseListener) {
        contentPanel.addMouseListener(mouseListener);
    }

    @Override
    public void addKeyListener(KeyListener keyListener) {
        contentPanel.addKeyListener(keyListener);
    }

    @Override
    public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        contentPanel.addMouseWheelListener(mouseWheelListener);
    }

    @Override
    public void addVisual(Visual v) {
        contentPanel.addVisual(v);
    }

    @Override
    public void run() {
        Timer frameTimer = new Timer();
        Timer secondTimer = new Timer();
        int frames = 0;
        frameTimer.startTiming();
        secondTimer.startTiming();
        while(!close) {
            if(frameTimer.getTime() >= 1000/60) {
                repaint();
                frameTimer.startTiming();
                frames ++;
            }
            if(secondTimer.getTime() >= 1000) {
                Debugger.log("Frames per second: " + frames, Debugger.Tag.FPS);
                frames = 0;
                secondTimer.startTiming();
            }
        }
    }
}
