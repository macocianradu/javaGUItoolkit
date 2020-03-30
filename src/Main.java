import guiTree.Animations.ColorAnimation;
import guiTree.Animations.LocationAnimation;
import guiTree.Components.Slider;
import guiTree.Helper.Point2;
import guiTree.Visual;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.*;
import java.awt.event.MouseEvent;


public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;
            Slider hslider = (Slider)window.findByName("hslider");
            Slider slider = (Slider)window.findByName("slider");
            window.repaint();
            ColorAnimation sliderColor = new ColorAnimation(hslider, slider.getBackgroundColor(), Color.RED, 1000);
            window.addMouseListener(new MouseAdapter() {
                private boolean out = false;
                private LocationAnimation outAnimation;
                private LocationAnimation inAnimation;
                @Override
                public void mouseMoved(MouseEvent mouseEvent) {
                    if(mouseEvent.getX() < 20 && mouseEvent.getY() > 0 && mouseEvent.getY() < 100) {
                        if(!out) {
                            outAnimation = new LocationAnimation(slider, slider.getLocation(), new Point2<>(0, slider.getLocationY()), 500);
                            window.removeAnimation(inAnimation);
                            window.addAnimation(outAnimation);
                            out = true;
                        }
                    }
                    else {
                        if(out) {
                            inAnimation = new LocationAnimation(slider, slider.getLocation(), new Point2<>(- 20, slider.getLocationY()), 500);
                            window.removeAnimation(outAnimation);
                            window.addAnimation(inAnimation);
                            out = false;
                        }
                    }
                }
            });
            Thread.sleep(5000);
            System.out.println("Started moving");
            hslider.addAnimation(new LocationAnimation(hslider, hslider.getLocation(),  new Point2<>(hslider.getLocationX(), hslider.getLocationY() + 100), 1000));
            hslider.addAnimation(sliderColor);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
