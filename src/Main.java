import guiTree.Components.Button;
import guiTree.Components.TitleBar;
import guiTree.Window;
import parser.XAMLParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;
            window.revalidate();
            Button button = (Button)window.findByName("button1");

            BufferedImage icon = null;
            try {
                icon = ImageIO.read(new File("resources\\icons\\square_white.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            TitleBar bar = new TitleBar("Working Title", icon);
            bar.setBackgroundColor(Color.GRAY);
            window.setTitleBar(bar);

            window.revalidate();
            long now;
            long prev = 0;
//            while(true) {
//                now = System.currentTimeMillis();
//                if(now - prev >= 1000) {
//                    int x = button.getLocationX();
//                    int y = button.getLocationY();
//                    if(x + button.getWidth() >= window.getWidth()) {
//                        x = 0;
//                        if(y + button.getHeight() >= window.getHeight()) {
//                            y = 0;
//                        }
//                        else {
//                            y += 30;
//                        }
//                    }
//                    else {
//                        x += 30;
//                    }
//                    button.setLocation(x, y);
//                    prev = now;
//                    window.revalidate();
//                }
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
