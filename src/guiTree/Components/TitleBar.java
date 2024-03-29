package guiTree.Components;

import guiTree.Visual;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class TitleBar extends Visual {
    public static final int CLOSE = 10;
    public static final int MINIMIZE = 11;
    public static final int NORMALIZE = 12;
    public static final int MAXIMIZE = 13;

    private BufferedImage icon;
    private String title;
    private Button minimize;
    private Button maximize;
    private Button close;


    /*--------------------------------------------------------------------
                            Constructors
    ---------------------------------------------------------------------*/
    public TitleBar() {
        this(null, null);
    }

    public TitleBar(String title) {
        this(title, null);
    }

    public TitleBar(BufferedImage icon) {
        this(null, icon);
    }

    public TitleBar(String title, BufferedImage icon) {
        super();
        this.icon = icon;
        this.title = title;

        this.close = new Button();
        this.minimize = new Button();
        this.maximize = new Button();
        close.setIcon("close_white");
        minimize.setIcon("minimize_white");
        maximize.setIcon("square_white");
        close.setSize(60, 30);
        minimize.setSize(60, 30);
        maximize.setSize(60, 30);
        close.setBackgroundColor(Color.GRAY);
        maximize.setBackgroundColor(Color.GRAY);
        minimize.setBackgroundColor(Color.GRAY);
        close.setAccentColor(Color.RED);
        maximize.setAccentColor(Color.LIGHT_GRAY);
        minimize.setAccentColor(Color.LIGHT_GRAY);
        close.setForegroundColor(Color.RED);
        maximize.setForegroundColor(Color.DARK_GRAY);
        minimize.setForegroundColor(Color.DARK_GRAY);


        this.setSize(0, 30);
        this.setLocation(0, 0);

        setButtonLocation();

        this.addVisual(close);
        this.addVisual(minimize);
        this.addVisual(maximize);
        minimize.setName("minimize");
        maximize.setName("maximize");
        close.setName("close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                notifyParent(CLOSE);
            }
        });
        maximize.addMouseListener(new MouseAdapter() {
            private boolean isMaximized = false;
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if(isMaximized) {
                    notifyParent(NORMALIZE);
                    maximize.setIcon("square_white");
                    isMaximized = false;
                }
                else {
                    maximize.setIcon("normalize_white");
                    notifyParent(MAXIMIZE);
                    isMaximized = true;
                }
            }
        });
        minimize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                notifyParent(MINIMIZE);
            }
        });
    }

    /*--------------------------------------------------------------------
                            Getters
    ---------------------------------------------------------------------*/

    public BufferedImage getIcon() {
        return icon;
    }

    public String geTitle() {
        return title;
    }


    /*--------------------------------------------------------------------
                            Setters
    ---------------------------------------------------------------------*/

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    @Override
    public void setBackgroundColor(Color color) {
        super.setBackgroundColor(color);
        close.setBackgroundColor(color);
        maximize.setBackgroundColor(color);
        minimize.setBackgroundColor(color);
    }

    @Override
    public void setAccentColor(Color color) {
        super.setAccentColor(color);
        close.setAccentColor(color);
        maximize.setAccentColor(color);
        minimize.setAccentColor(color);
    }

    @Override
    public void setForegroundColor(Color color) {
        super.setForegroundColor(color);
        close.setForegroundColor(color);
        maximize.setForegroundColor(color);
        minimize.setForegroundColor(color);
    }

    @Override
    public void setPaintColor(Color color) {
        super.setPaintColor(color);
        close.setPaintColor(color);
        maximize.setPaintColor(color);
        minimize.setPaintColor(color);
    }

    public void setTitle(String title) {
        this.title = title;
        update();
    }

    @Override
    public void setSize(Integer width, Integer height) {
        super.setSize(width, height);
        setButtonLocation();
    }

    @Override
    public void paint(Image imageBuffer) {
        Graphics2D g = (Graphics2D)imageBuffer.getGraphics();
        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D iconGraphics = (Graphics2D)imageBuffer.getGraphics();
        iconGraphics.drawImage(icon, 5, (getHeight() - icon.getHeight())/2, null);
        iconGraphics.dispose();

        if(getFont() != null) {
            g.setFont(getFont());
        }
        int stringOffset = icon.getWidth() + 10;
        int textHeight = 0;
        if(!title.equals("")) {
            textHeight = g.getFontMetrics().getHeight();
            g.setColor(Color.WHITE);
        }
        g.drawString(title, stringOffset, getHeight()/2 + textHeight/4);
        g.dispose();
    }

    private void setButtonLocation() {
        int buttonOffset = this.getWidth() - close.getWidth();
        close.setLocation(buttonOffset, 0);
        buttonOffset -= maximize.getWidth();
        maximize.setLocation(buttonOffset, 0);
        buttonOffset -= minimize.getWidth();
        minimize.setLocation(buttonOffset, 0);
    }
}
