import guiTree.Button;
import guiTree.Window;
import parser.XAMLParser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;
            window.revalidate();
            Button button = (Button)window.findByName("button1");
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    window.dispose();
                }
            });
            long now;
            long prev = 0;
            while(true) {
                now = System.currentTimeMillis();
                if(now - prev >= 1000) {
                    int x = button.getLocationX();
                    int y = button.getLocationY();
                    if(x + button.getWidth() >= window.getWidth()) {
                        x = 0;
                        if(y + button.getHeight() >= window.getHeight()) {
                            y = 0;
                        }
                        else {
                            y += 30;
                        }
                    }
                    else {
                        x += 30;
                    }
                    button.setLocation(x, y);
                    prev = now;
                    window.revalidate();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
