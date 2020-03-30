package guiTree.Components;

import guiTree.Visual;
import guiTree.events.MouseAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Slider extends Visual {
    public enum Direction {
        Vertical,
        Horizontal
    }

    private Direction direction;
    private Button slider;
    private Button button2;
    private Button button1;
    private SliderListener sliderListener;

    public Slider() {
        this(Direction.Vertical);
    }

    public Slider(Direction direction) {
        super();
        this.direction = direction;

        button1 = new Button();
        button2 = new Button();
        slider = new Button();

        setBackgroundColor(new Color(175, 175, 175));
        setForegroundColor(new Color(112, 112, 112));
        setAccentColor(new Color(50, 50, 50));
        button1.setHasBorder(false);
        button2.setHasBorder(false);

        if(direction == Direction.Horizontal) {
            button1.setIcon("arrow_left_black");
            button2.setIcon("arrow_right_black");
            button1.setLocation(0.0f, 0.15f);
            button2.setLocation(-1.0f, 0.15f);
            slider.setLocation(-1.0f, 0.25f);
            slider.setSize(-1.0f, 0.5f);
            slider.setLocation(20, 0);
        }
        else {
            button1.setIcon("arrow_up_black");
            button2.setIcon("arrow_down_black");
            button1.setLocation(0.15f, 0.0f);
            button2.setLocation(0.15f, -1.0f);
            slider.setLocation(0.25f, -1.0f);
            slider.setSize(0.5f, -1.0f);
            slider.setLocation(0, 20);
        }

        addVisual(button1);
        addVisual(button2);
        addVisual(slider);

        button1.setLocation(0, 0);
        slider.setRound(10);
        button1.setRound(10);
        button2.setRound(10);

        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                moveSlider(-0.05f);
                button1.update();
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                moveSlider(0.05f);
                button2.update();
            }
        });
        sliderListener = new SliderListener();
        slider.addMouseListener(sliderListener);
    }

    public float getSliderLocation() {
        if(direction == Direction.Vertical) {
            return (float)slider.getLocationY() / getHeight();
        }
        else {
            return (float)slider.getLocationX() / getWidth();
        }
    }

    public void setSliderSize(int width, int height) {
        slider.setSize(width, height);
    }

    private void moveSlider(int offset) {
        if(direction == Direction.Vertical) {
            if(slider.getLocationY() + slider.getHeight() + offset > getHeight() - button2.getHeight()) {
                slider.setLocationY(getHeight() - slider.getHeight() - button2.getHeight());
            }
            else if (slider.getLocationY() + offset < button1.getHeight()) {
                slider.setLocationY(button1.getHeight());
            }
            else {
                slider.setLocationY(Math.round(slider.getLocationY() + offset));
            }
        }
        else {
            if(slider.getLocationX() + slider.getWidth() + offset > getWidth() - button2.getWidth()) {
                slider.setLocationX(getWidth() - slider.getWidth() - button2.getWidth());
            }
            else if (slider.getLocationX() + offset < button1.getWidth()) {
                slider.setLocationX(button1.getWidth());
            }
            else {
                slider.setLocationX(Math.round(slider.getLocationX() + offset));
            }
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if(direction == Direction.Horizontal) {
            button1.setIcon("arrow_left_black");
            button2.setIcon("arrow_right_black");
            button1.setLocation(0.0f, 0.15f);
            button2.setLocation(-1.0f, 0.15f);
            slider.setLocation(-1.0f, 0.25f);
            slider.setSize(-1.0f, 0.5f);
            slider.setLocation(20, 0);
        }
        else {
            button1.setIcon("arrow_up_black");
            button2.setIcon("arrow_down_black");
            button1.setLocation(0.15f, 0.0f);
            button2.setLocation(0.15f, -1.0f);
            slider.setLocation(0.25f, -1.0f);
            slider.setSize(0.5f, -1.0f);
            slider.setLocation(0, 20);
        }
    }

    private void moveSlider(float offset) {
        if(direction == Direction.Vertical) {
            moveSlider(Math.round(offset * getHeight()));
            return;
        }
        moveSlider(Math.round(offset * getWidth()));
    }

    @Override
    public void setBackgroundColor(Color color) {
        super.setBackgroundColor(color);
        button1.setBackgroundColor(new Color(0, 0, 0, 0.0f));
        button2.setBackgroundColor(new Color(0, 0, 0, 0.0f));
    }

    @Override
    public void setForegroundColor(Color color) {
        super.setForegroundColor(color);
        slider.setBackgroundColor(color);
        slider.setAccentColor(color);
        button1.setAccentColor(color);
        button2.setAccentColor(color);
    }

    @Override
    public void setAccentColor(Color color) {
        super.setAccentColor(color);
        button1.setForegroundColor(color);
        button2.setForegroundColor(color);
        slider.setForegroundColor(color);
    }

    @Override
    public void setBorderColor(Color color) {
        super.setBorderColor(color);
        button1.setBorderColor(color);
        button2.setBorderColor(color);
        slider.setBorderColor(color);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        slider.setName(name + " slider");
        button1.setName(name + " button 1");
        button2.setName(name + " button 2");
    }

    @Override
    public void setSize() {
        super.setSize();
        if(direction == Direction.Vertical) {
            button1.setSize(Math.round(0.7f * getWidth()), Math.round(0.7f * getWidth()));
            button2.setSize(button1.getWidth(), button1.getHeight());
            button2.setLocation(0, getHeight() - button2.getHeight());
            slider.setSize(getWidth() / 2, 40);
        }
        else {
            button1.setSize(Math.round(0.7f * getHeight()), Math.round(0.7f * getHeight()));
            button2.setSize(button1.getWidth(), button1.getHeight());
            button2.setLocation(getWidth() - button2.getWidth(), 0);
            slider.setSize(40, getHeight() / 2);
        }
    }

    @Override
    public void paint(BufferedImage imageBuffer) {
        Graphics2D g = imageBuffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(getHasBorder()) {
            g.setColor(getBorderColor());
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        g.setColor(getBackgroundColor());
        if(direction == Direction.Vertical) {
            int x1 = Math.round(0.15f * getWidth());
            int y1 = button1.getHeight();
            int x2 = Math.round(0.85f * getWidth()) - x1;
            int y2 = getHeight() - button2.getHeight() - y1;
            g.fillRoundRect(x1, y1, x2, y2, 10, 10);
        }
        else {
            int x1 = button1.getWidth();
            int y1 = Math.round(0.15f * getHeight());
            int x2 = getWidth() - button2.getWidth() - x1;
            int y2 = Math.round(0.85f * getHeight()) - y1;
            g.fillRoundRect(x1, y1, x2, y2, 10, 10);
        }
        g.dispose();
    }

    private class SliderListener extends MouseAdapter {
        private int start;

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if(direction == Direction.Vertical) {
                start = mouseEvent.getY();
            }
            else {
                start = mouseEvent.getX();
            }
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            if(direction == Direction.Vertical) {
                moveSlider(mouseEvent.getY() - start);
                start = mouseEvent.getY();
            }
            else {
                moveSlider(mouseEvent.getX() - start);
                start = mouseEvent.getX();
            }
            slider.update();
        }
    }
}
