package guiTree;

import guiTree.Animations.AnimationInterface;
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
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

public class Window extends Visual implements Runnable{
    public static final int BRING_TO_FRONT = 100;
    public CustomFrame frame;
    private int FPS;
    private TitleBar titleBar;
    private Panel mainPanel;
    private Panel contentPanel;
    private Point2<Integer> oldSize;
    private List<AnimationInterface> animations;
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
        animations = new ArrayList<>();

        this.setMainPanel(mainPanel);

        BufferedImage icon = null;
        try {
            InputStream iconStream = getClass().getClassLoader().getResourceAsStream("icons/square_white.png");
            assert iconStream != null;
            icon = ImageIO.read(iconStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TitleBar bar = new TitleBar(title, icon);
        bar.setName("TitleBar");
        FPS = 60;
        bar.setBackgroundColor(Color.GRAY);
        this.setTitleBar(bar);
        close = false;

        Thread paintThread = new Thread(this);
        paintThread.setName("Painting Thread");
        paintThread.start();
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
    }

    public void setFrameImageBuffer(Image imageBuffer){
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

    public void dispose() {
        close = true;
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

    public void setFPS(int FPS) {
        this.FPS = FPS;
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
        mainPanel.setOverlapping(true);
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

    public void setContentPanel(Panel contentPanel) {
        mainPanel.removeVisual(this.contentPanel);
        contentPanel.setName("ContentPanel");

        if(titleBar != null) {
            mainPanel.addVisual(titleBar);
            contentPanel.setLocation(0, titleBar.getHeight());
            contentPanel.setSize(mainPanel.getWidth(), mainPanel.getHeight() - titleBar.getHeight());
        }
        else {
            contentPanel.setLocation(0, 0);
            contentPanel.setSize(mainPanel.getWidth(), mainPanel.getHeight());
        }

        mainPanel.addVisual(contentPanel);
        this.contentPanel = contentPanel;
    }

    public Panel getContentPanel() {
        return contentPanel;
    }

    public void setTitleBarBackgroundColor(Color color) {
        titleBar.setBackgroundColor(color);
    }

    public void setTitleBarAccentColor(Color color) {
        titleBar.setAccentColor(color);
    }

    public void setTitleBarForegroundColor(Color color) {
        titleBar.setForegroundColor(color);
    }

    @Override
    public void setBackgroundColor(Color color) {
        contentPanel.setBackgroundColor(color);
    }

    @Override
    public void handleNotification(Visual v, int notify) {
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

    public void setCursor(Cursor cursor) {
        frame.setCursor(cursor);
    }

    @Override
    public void addAnimation(AnimationInterface animation) {
        animations.add(animation);
    }

    @Override
    public void removeAnimation(AnimationInterface animation) {
        animations.remove(animation);
    }

    @Override
    public void removeAllAnimations() {
        animations.clear();
    }

    @Override
    public void run() {
        Timer frameTimer = new Timer();
        Timer secondTimer = new Timer();
        int frames = 0;
        frameTimer.startTiming();
        secondTimer.startTiming();
        while(!close) {
            if(frameTimer.getTime() >= 1000/FPS) {
                for(int i = 0; i < animations.size(); i++) {
                    if(animations.get(i).step()) {
                        animations.remove(animations.get(i));
                        i--;
                    }
                }
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
