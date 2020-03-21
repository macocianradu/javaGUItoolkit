import guiTree.Components.Button;
import guiTree.Components.Panel;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.event.MouseEvent;


public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;
            window.repaint();
            Button button = (Button)window.findByName("button3");
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    Panel panel = window.getMainPanel();
                    if(panel.getOverlapping()) {
                        panel.setOverlapping(false);
                    }
                    else {
                        panel.setOverlapping(true);
                    }
                    window.repaint();
                }
            });

            window.repaint();
            System.out.println(Float.parseFloat("3"));
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
