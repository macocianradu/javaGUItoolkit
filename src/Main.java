import guiTree.Components.Button;
import guiTree.Components.DropDown;
import guiTree.Components.Text;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        Window window = null;
        try {
            window = XAMLParser.parse("ui.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert window != null;
        Button alignToLeft  = (Button) window.findByName("align_to_left");
        Button alignToRight  = (Button) window.findByName("align_to_right");
        Button alignToCenter  = (Button) window.findByName("align_to_center");
        Text textField = (Text) window.findByName("Text");

        alignToLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                textField.setAlignment("left");
                textField.update();
            }
        });

        alignToCenter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                textField.setAlignment("center");
                textField.update();
            }
        });

        alignToRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                textField.setAlignment("right");
                textField.update();
            }
        });

    }
}
