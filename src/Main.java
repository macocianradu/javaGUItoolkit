import guiTree.Components.Button;
import guiTree.Components.CheckBox;
import guiTree.Components.ToggleButton;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("ui.xml");
            assert window != null;

//            Button button1 = (Button)window.findByName("button1");
//            button1.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent mouseEvent) {
//                    System.out.println("Button x: " + button1.getLocationX() + " y: " + button1.getLocationY());
//                }
//            });
//
//            ToggleButton button2 = (ToggleButton)window.findByName("button2");
//            button2.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent mouseEvent) {
//                    System.out.println("Button x: " + button2.getLocationX() + " y: " + button2.getLocationY());
//                }
//            });
//
//            Button button4 = (Button)window.findByName("button4");
//            button4.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent mouseEvent) {
//                    System.out.println("Button x: " + button4.getLocationX() + " y: " + button4.getLocationY());
//                }
//            });
            CheckBox checkBox = (CheckBox)window.findByName("checkbox");
            window.repaint();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
