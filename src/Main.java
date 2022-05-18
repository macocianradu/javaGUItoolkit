import guiTree.Animations.ColorAnimation;
import guiTree.Components.Button;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XMLParser;

import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        Window window = null;
        try {
            window = (Window)XMLParser.parse("otherui.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
