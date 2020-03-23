import guiTree.Components.Button;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;

            Button button1 = (Button)window.findByName("button1");
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    System.out.println("Button x: " + button1.getLocationX() + " y: " + button1.getLocationY());
                }
            });

            Button button2 = (Button)window.findByName("button2");
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    System.out.println("Button x: " + button2.getLocationX() + " y: " + button2.getLocationY());
                }
            });

            Button button4 = (Button)window.findByName("button4");
            button4.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    System.out.println("Button x: " + button4.getLocationX() + " y: " + button4.getLocationY());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
