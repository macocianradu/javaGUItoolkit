import guiTree.Window;
import parser.XAMLParser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        try{
            Window window = XAMLParser.parse("company.xml");
            assert window != null;
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    window.dispose();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
