import guiTree.Components.Button;
import guiTree.Components.Panel;
import guiTree.Components.Picture;
import guiTree.Visual;
import guiTree.Window;
import guiTree.events.MouseAdapter;
import parser.XAMLParser;

import java.awt.event.MouseEvent;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Window window = null;
        Visual.setEnableGPU(true);
        try {
            window = XAMLParser.parse("otherui.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
